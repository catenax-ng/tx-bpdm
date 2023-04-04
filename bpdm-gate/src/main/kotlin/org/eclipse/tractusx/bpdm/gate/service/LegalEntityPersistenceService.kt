/*******************************************************************************
 * Copyright (c) 2021,2023 Contributors to the Eclipse Foundation
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

package org.eclipse.tractusx.bpdm.gate.service

import org.eclipse.tractusx.bpdm.gate.dto.LegalEntityGateInputRequest
import org.eclipse.tractusx.bpdm.gate.entity.LegalEntityGate
import org.eclipse.tractusx.bpdm.gate.repository.GateLegalEntityRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class LegalEntityPersistenceService(
    private val gateLegalEntityRepository: GateLegalEntityRepository
) {

    @Transactional
    fun persistLegalEntytiesBP(legalEntities: Collection<LegalEntityGateInputRequest>) {

        //finds Legal Entity by External ID
        val legalEntityRecord  = gateLegalEntityRepository.findDistinctByBpnIn(legalEntities.map { it.externalId })

        //Business Partner persist
        legalEntities.forEach { legalEntity ->
            val fullLegalEntity = legalEntity.toLegalEntityGate()
            legalEntityRecord.find { it.externalId == legalEntity.externalId }?.let { existingLegalEntity ->
                updateLegalEntity(existingLegalEntity, legalEntity)
                gateLegalEntityRepository.save(existingLegalEntity)
            } ?: run {
                gateLegalEntityRepository.save(fullLegalEntity)
            }

        }
    }


    private fun updateLegalEntity(legalEntity: LegalEntityGate, legalEntityRequest: LegalEntityGateInputRequest): LegalEntityGate {
        legalEntity.bpn = legalEntityRequest.bpn.toString();
        legalEntity.legalForm = legalEntityRequest.legalEntity.legalForm.toString();
        legalEntity.types = legalEntityRequest.legalEntity.types.toMutableSet();
        legalEntity.legalAddress = legalEntityRequest.legalEntity.legalAddress.toAddressGateDto()
        legalEntity.externalId = legalEntityRequest.externalId
        legalEntity.identifiers.replace(legalEntityRequest.legalEntity.identifiers.map { toEntity(it,legalEntity) });
        legalEntity.nameGates.replace(legalEntityRequest.legalEntity.names.map {toEntity(it, legalEntity)}.toSet());
        legalEntity.bankAccounts.replace(legalEntityRequest.legalEntity.bankAccounts.map { toEntity(it,legalEntity) }.toSet())
        legalEntity.classification.replace(legalEntityRequest.legalEntity.profileClassifications.map { toEntity(it, legalEntity) }.toSet())
        return legalEntity;
    }
    private fun <T> MutableCollection<T>.replace (elements : Collection<T>) {
        clear()
        addAll(elements)
    }
}