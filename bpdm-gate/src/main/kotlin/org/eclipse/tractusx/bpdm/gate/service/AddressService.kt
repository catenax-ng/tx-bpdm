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

import mu.KotlinLogging
import org.eclipse.tractusx.bpdm.common.dto.response.PageResponse
import org.eclipse.tractusx.bpdm.common.exception.BpdmNotFoundException
import org.eclipse.tractusx.bpdm.common.model.OutputInputEnum
import org.eclipse.tractusx.bpdm.gate.api.model.*
import org.eclipse.tractusx.bpdm.gate.config.BpnConfigProperties
import org.eclipse.tractusx.bpdm.gate.entity.ChangelogEntry
import org.eclipse.tractusx.bpdm.gate.entity.LogisticAddress
import org.eclipse.tractusx.bpdm.gate.repository.ChangelogRepository
import org.eclipse.tractusx.bpdm.gate.repository.GateAddressRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service

@Service
class AddressService(
    private val bpnConfigProperties: BpnConfigProperties,
    private val changelogRepository: ChangelogRepository,
    private val addressPersistenceService: AddressPersistenceService,
    private val addressRepository: GateAddressRepository
) {
    private val logger = KotlinLogging.logger { }

    fun getAddresses(page: Int, size: Int, externalIds: Collection<String>? = null): PageResponse<AddressGateInputResponse> {

        val logisticAddressPage = if (externalIds != null) {
            addressRepository.findByExternalIdInAndDataType(externalIds, OutputInputEnum.Input, PageRequest.of(page, size))
        } else {
            addressRepository.findByDataType(OutputInputEnum.Input, PageRequest.of(page, size))
        }

        return PageResponse(
            page = page,
            totalElements = logisticAddressPage.totalElements,
            totalPages = logisticAddressPage.totalPages,
            contentSize = logisticAddressPage.content.size,
            content = toValidLogisticAddresses(logisticAddressPage),
        )
    }

    private fun toValidLogisticAddresses(logisticAddressPage: Page<LogisticAddress>): List<AddressGateInputResponse> {
        return logisticAddressPage.content.map { logisticAddress ->
            logisticAddress.toAddressGateInputResponse(logisticAddress)
        }
    }

    fun getAddressByExternalId(externalId: String): AddressGateInputResponse {

        val logisticAddress =
            addressRepository.findByExternalIdAndDataType(externalId, OutputInputEnum.Input) ?: throw BpdmNotFoundException("Logistic Address", externalId)

        return logisticAddress.toAddressGateInputResponse(logisticAddress)

    }

    /**
     * Get output addresses by fetching addresses from the database.
     */
    fun getAddressesOutput(externalIds: Collection<String>? = null, page: Int, size: Int): PageResponse<AddressGateOutputResponse> {

        val logisticAddressPage = if (externalIds != null && externalIds.isNotEmpty()) {
            addressRepository.findByExternalIdInAndDataType(externalIds, OutputInputEnum.Output, PageRequest.of(page, size))
        } else {
            addressRepository.findByDataType(OutputInputEnum.Output, PageRequest.of(page, size))
        }

        return PageResponse(
            page = page,
            totalElements = logisticAddressPage.totalElements,
            totalPages = logisticAddressPage.totalPages,
            contentSize = logisticAddressPage.content.size,
            content = toValidOutputLogisticAddresses(logisticAddressPage),
        )

    }

    private fun toValidOutputLogisticAddresses(logisticAddressPage: Page<LogisticAddress>): List<AddressGateOutputResponse> {
        return logisticAddressPage.content.map { logisticAddress ->
            logisticAddress.toAddressGateOutputResponse(logisticAddress)
        }
    }

    /**
     * Upsert addresses input to the database
     **/
    fun upsertAddresses(addresses: Collection<AddressGateInputRequest>) {

        // create changelog entry if all goes well from saasClient
        addresses.forEach { address ->
            changelogRepository.save(ChangelogEntry(address.externalId, LsaType.ADDRESS))
        }

        addressPersistenceService.persistAddressBP(addresses, OutputInputEnum.Input)
    }

    /**
     * Upsert addresses output to the database
     **/
    fun upsertOutputAddresses(addresses: Collection<AddressGateOutputRequest>) {

        addressPersistenceService.persistOutputAddressBP(addresses, OutputInputEnum.Output)

    }


}