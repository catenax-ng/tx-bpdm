/*******************************************************************************
 * Copyright (c) 2021,2024 Contributors to the Eclipse Foundation
 *
 * See the NOTICE file(s) distributed with this work for additional
 * information regarding copyright ownership.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Apache License, Version 2.0 which is available at
 * https://www.apache.org/licenses/LICENSE-2.0.
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 *
 * SPDX-License-Identifier: Apache-2.0
 ******************************************************************************/

package org.eclipse.tractusx.bpdm.orchestrator.service

import mu.KotlinLogging
import org.eclipse.tractusx.bpdm.orchestrator.config.TaskConfigProperties
import org.eclipse.tractusx.bpdm.orchestrator.exception.BpdmEmptyResultException
import org.eclipse.tractusx.bpdm.orchestrator.exception.BpdmTaskNotFoundException
import org.eclipse.tractusx.bpdm.orchestrator.model.GoldenRecordTask
import org.eclipse.tractusx.bpdm.orchestrator.model.TaskProcessingState
import org.eclipse.tractusx.orchestrator.api.model.*
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import java.time.Instant
import java.util.*

@Service
class GoldenRecordTaskService(
    private val taskStorage: GoldenRecordTaskStorage,
    private val goldenRecordTaskStateMachine: GoldenRecordTaskStateMachine,
    private val taskConfigProperties: TaskConfigProperties
) {

    private val logger = KotlinLogging.logger { }

    @Synchronized
    fun createTasks(createRequest: TaskCreateRequest): TaskCreateResponse {
        logger.debug { "Creation of new golden record tasks: executing createTasks() with parameters $createRequest" }
        return createRequest.businessPartners
            .map { businessPartnerGeneric -> taskStorage.addTask(initTask(createRequest, businessPartnerGeneric)) }
            .map(::toTaskClientStateDto)
            .let { TaskCreateResponse(createdTasks = it) }
    }

    @Synchronized
    fun searchTaskStates(stateRequest: TaskStateRequest): TaskStateResponse {
        logger.debug { "Search for the state of golden record task: executing searchTaskStates() with parameters $stateRequest" }
        return stateRequest.taskIds
            .mapNotNull { taskId -> taskStorage.getTask(taskId) }       // skip missing tasks
            .map(::toTaskClientStateDto)
            .let { TaskStateResponse(tasks = it) }
    }

    @Synchronized
    fun reserveTasksForStep(reservationRequest: TaskStepReservationRequest): TaskStepReservationResponse {
        logger.debug { "Reservation of next golden record tasks: executing reserveTasksForStep() with parameters $reservationRequest" }
        val now = Instant.now()

        val tasks = taskStorage.getQueuedTasksByStep(reservationRequest.step, reservationRequest.amount)
        tasks.forEach { task -> goldenRecordTaskStateMachine.doReserve(task) }

        val pendingTimeout = tasks.minOfOrNull { calculateTaskPendingTimeout(it.processingState) } ?: now

        val taskEntries = tasks.map { task ->
            TaskStepReservationEntry(
                taskId = task.taskId,
                businessPartner = task.businessPartner
            )
        }

        return TaskStepReservationResponse(
            reservedTasks = taskEntries,
            // property is deprecated
            timeout = pendingTimeout
        )
    }

    @Synchronized
    fun resolveStepResults(resultRequest: TaskStepResultRequest) {
        logger.debug { "Step results for reserved golden record tasks: executing resolveStepResults() with parameters $resultRequest" }
        resultRequest.results
            .forEach { resultEntry ->
                val task = taskStorage.getTask(resultEntry.taskId)
                    ?: throw BpdmTaskNotFoundException(resultEntry.taskId)
                val step = resultRequest.step
                val errors = resultEntry.errors
                val resultBusinessPartner = resultEntry.businessPartner

                if (errors.isNotEmpty()) {
                    goldenRecordTaskStateMachine.doResolveTaskToError(task, step, errors)
                } else if (resultBusinessPartner != null) {
                    goldenRecordTaskStateMachine.doResolveTaskToSuccess(task, step, resultBusinessPartner)
                } else {
                    throw BpdmEmptyResultException(resultEntry.taskId)
                }
            }
    }

    @Scheduled(cron = "\${bpdm.task.timeoutCheckCron}")
    @Synchronized
    fun checkForTimeouts() {
        try {
            logger.debug { "Checking for timeouts" }
            checkForPendingTimeouts()
            checkForRetentionTimeouts()
        } catch (err: RuntimeException) {
            logger.error(err) { "Error checking for timeouts" }
        }
    }

    private fun checkForPendingTimeouts() {
        taskStorage.getTasksWithPendingTimeoutBefore(Instant.now())
            .forEach {
                try {
                    logger.info { "Setting timeout for task ${it.taskId} after reaching pending timeout" }
                    goldenRecordTaskStateMachine.doResolveTaskToTimeout(it)
                } catch (err: RuntimeException) {
                    logger.error(err) { "Error handling pending timeout for task ${it.taskId}" }
                }
            }
    }

    private fun checkForRetentionTimeouts() {
        taskStorage.getTasksWithRetentionTimeoutBefore(Instant.now())
            .forEach {
                try {
                    logger.info { "Removing task ${it.taskId} after reaching retention timeout" }
                    taskStorage.removeTask(it.taskId)
                } catch (err: RuntimeException) {
                    logger.error(err) { "Error handling retention timeout for task ${it.taskId}" }
                }
            }
    }

    private fun initTask(
        createRequest: TaskCreateRequest,
        businessPartnerGeneric: BusinessPartnerGeneric
    ) = GoldenRecordTask(
        taskId = UUID.randomUUID().toString(),
        businessPartner = BusinessPartnerFull(
            generic = businessPartnerGeneric
        ),
        processingState = goldenRecordTaskStateMachine.initProcessingState(createRequest.mode)
    )

    private fun toTaskClientStateDto(task: GoldenRecordTask): TaskClientState {
        val businessPartnerResult = when (task.processingState.resultState) {
            ResultState.Success -> task.businessPartner.generic
            else -> null
        }
        return TaskClientState(
            taskId = task.taskId,
            processingState = toTaskProcessingStateDto(task.processingState),
            businessPartnerResult = businessPartnerResult
        )
    }

    private fun toTaskProcessingStateDto(processingState: TaskProcessingState): org.eclipse.tractusx.orchestrator.api.model.TaskProcessingState {
        return TaskProcessingState(
            resultState = processingState.resultState,
            step = processingState.step,
            stepState = processingState.stepState,
            errors = processingState.errors,
            createdAt = processingState.taskCreatedAt,
            modifiedAt = processingState.taskModifiedAt,
            // property is deprecated
            timeout = calculateTaskRetentionTimeout(processingState)
        )
    }

    private fun calculateTaskPendingTimeout(processingState: TaskProcessingState) =
        processingState.taskCreatedAt.plus(taskConfigProperties.taskPendingTimeout)

    private fun calculateTaskRetentionTimeout(processingState: TaskProcessingState) =
        processingState.taskCreatedAt.plus(taskConfigProperties.taskRetentionTimeout)
}
