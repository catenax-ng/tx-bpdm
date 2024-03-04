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

package com.catenax.bpdm.bridge.dummy.service

import com.catenax.bpdm.bridge.dummy.dto.*
import mu.KotlinLogging
import org.eclipse.tractusx.bpdm.common.dto.BusinessPartnerType
import org.eclipse.tractusx.bpdm.common.exception.BpdmNullMappingException
import org.eclipse.tractusx.bpdm.gate.api.client.GateClient
import org.eclipse.tractusx.bpdm.pool.api.client.PoolApiClient
import org.eclipse.tractusx.bpdm.pool.api.model.LogisticAddress
import org.eclipse.tractusx.bpdm.pool.api.model.Site
import org.eclipse.tractusx.bpdm.pool.api.model.request.*
import org.eclipse.tractusx.bpdm.pool.api.model.response.*
import org.springframework.stereotype.Service

@Service
class PoolUpdateService(
    private val gateQueryService: GateQueryService,
    private val poolClient: PoolApiClient,
    private val gateClient: GateClient
) {

    private val logger = KotlinLogging.logger { }

    fun createLegalEntitiesInPool(entriesToCreate: Collection<GateLegalEntityInfo>): LegalEntityPartnerCreateResponseWrapper {
        val errorListException = mutableListOf<ErrorInfo<LegalEntityCreateError>>()
        val createRequests = mutableListOf<LegalEntityPartnerCreateRequest>()

        entriesToCreate.forEach {
            try {
                createRequests.add( LegalEntityPartnerCreateRequest(
                    legalEntity = gateToPoolLegalEntity(it.legalEntity),
                    legalAddress = gateToPoolLogisticAddress(it.legalAddress.address),
                    index = it.externalId)
                )
            }catch (e: BpdmNullMappingException){
                val errorMapped = mapError(it, e.message!!,LegalEntityCreateError.LegalEntityErrorMapping)
                errorListException.add(errorMapped)
            }
        }

        val result = poolClient.legalEntities.createBusinessPartners(createRequests)
        val createdResult = result.copy(errors = result.errors + errorListException)
        logger.info { "Pool accepted ${createdResult.entityCount} new legal entities, ${createdResult.errorCount} were refused" }

        return createdResult
    }

    fun updateLegalEntitiesInPool(entriesToUpdate: Collection<GateLegalEntityInfo>): LegalEntityPartnerUpdateResponseWrapper {
        val errorListException = mutableListOf<ErrorInfo<LegalEntityUpdateError>>()
        val updateRequests = mutableListOf<LegalEntityPartnerUpdateRequest>()

        entriesToUpdate.forEach {
            try {
                updateRequests.add(
                    LegalEntityPartnerUpdateRequest(
                        legalEntity = gateToPoolLegalEntity(it.legalEntity),
                        legalAddress = gateToPoolLogisticAddress(it.legalAddress.address),
                        bpnl = it.bpn!!
                    )
                )
            } catch (e: BpdmNullMappingException) {
                val errorMapped = mapError(it, e.message!!,LegalEntityUpdateError.LegalEntityErrorMapping)
                errorListException.add(errorMapped)
            }
        }

        val result = poolClient.legalEntities.updateBusinessPartners(updateRequests)
        val updatedResult = result.copy(errors = result.errors + errorListException)
        logger.info { "Pool accepted ${updatedResult.entityCount} updated legal entities, ${updatedResult.errorCount} were refused" }

        return updatedResult
    }

    private fun <T : ErrorCode> mapError(
        record: GateLegalEntityInfo,
        message: String,
        errorCode: T
    ): ErrorInfo<T> {
        logger.info { "Error processing ${record.externalId}" }
        logger.error { "Error message: $message" }
        return ErrorInfo(
            errorCode,
            "Error on mapping '${record.legalEntity}' with bpn ${record.bpn} and externalId ${record.externalId}",
            record.bpn
        )
    }


    fun createSitesInPool(entriesToCreate: Collection<GateSiteInfo>): SitePartnerCreateResponseWrapper {
        val leParentBpnByExternalId = entriesToCreate
            .map { it.legalEntityExternalId }
            .let { gateQueryService.getBpnByExternalId(BusinessPartnerType.LEGAL_ENTITY, it.toSet()) }
        val createRequests = entriesToCreate.mapNotNull { entry ->
            leParentBpnByExternalId[entry.legalEntityExternalId]
                ?.let { leParentBpn ->
                    SitePartnerCreateRequest(
                        site = Site(
                            name = entry.site.nameParts.firstOrNull() ?: "",
                            states = entry.site.states.map(::gateToPoolSiteState),
                            mainAddress = gateToPoolLogisticAddress(entry.mainAddress),
                            confidenceCriteria = dummyConfidenceCriteria
                        ),
                        index = entry.externalId,
                        bpnlParent = leParentBpn
                    )
                }
        }

        if (createRequests.size != entriesToCreate.size) {
            logger.warn {
                "Not all found Gate sites (${entriesToCreate.size}) are passed to the Pool (only ${createRequests.size}) " +
                        "because some parent BPN-L entries are missing!"
            }
        }
        return poolClient.sites.createSite(createRequests)
            .also { logger.info { "Pool accepted ${it.entityCount} new sites, ${it.errorCount} were refused" } }
    }

    fun updateSitesInPool(entriesToUpdate: Collection<GateSiteInfo>): SitePartnerUpdateResponseWrapper {
        val updateRequests = entriesToUpdate.map {
            SitePartnerUpdateRequest(
                site = Site(
                    name = it.site.nameParts.firstOrNull() ?: "",
                    states = it.site.states.map(::gateToPoolSiteState),
                    mainAddress = gateToPoolLogisticAddress(it.mainAddress),
                    confidenceCriteria = dummyConfidenceCriteria
                ),
                bpns = it.bpn!!
            )
        }

        return poolClient.sites.updateSite(updateRequests)
            .also { logger.info { "Pool accepted ${it.entityCount} updated sites, ${it.errorCount} were refused" } }
    }

    fun createAddressesInPool(entriesToCreate: Collection<GateAddressInfo>): AddressPartnerCreateResponseWrapper {
        val leParentBpnByExternalId = entriesToCreate
            .mapNotNull { it.legalEntityExternalId }
            .let { gateQueryService.getBpnByExternalId(BusinessPartnerType.LEGAL_ENTITY, it.toSet()) }
        val leParentsCreateRequests = entriesToCreate
            .filter { !isLegalAddress(it) }
            .mapNotNull { entry ->
            leParentBpnByExternalId[entry.legalEntityExternalId]
                ?.let { leParentBpn ->
                    AddressPartnerCreateRequest(
                        address = gateToPoolLogisticAddress(entry.address),
                        index = entry.externalId,
                        bpnParent = leParentBpn
                    )
                }
        }

        val siteParentBpnByExternalId = entriesToCreate
            .mapNotNull { it.siteExternalId }
            .let { gateQueryService.getBpnByExternalId(BusinessPartnerType.SITE, it.toSet()) }
        val siteParentsCreateRequests = entriesToCreate
            .filter { !isSiteMainAddress(it) }
            .mapNotNull { entry ->
            siteParentBpnByExternalId[entry.siteExternalId]
                ?.let { siteParentBpn ->
                    AddressPartnerCreateRequest(
                        address = LogisticAddress(
                            name = entry.address.nameParts.firstOrNull(),
                            states = entry.address.states.map(::gateToPoolAddressState),
                            identifiers = entry.address.identifiers.map(::gateToPoolAddressIdentifier),
                            physicalPostalAddress = gateToPoolPhysicalAddress(entry.address.physicalPostalAddress),
                            alternativePostalAddress = entry.address.alternativePostalAddress?.let(::gateToPoolAlternativeAddress),
                            confidenceCriteria = dummyConfidenceCriteria
                        ),
                        index = entry.externalId,
                        bpnParent = siteParentBpn
                    )
                }
        }

        val createRequests = leParentsCreateRequests.plus(siteParentsCreateRequests)
        if (createRequests.size != entriesToCreate.size) {
            logger.warn {
                "Not all found Gate addresses (${entriesToCreate.size}) are passed to the Pool (only ${createRequests.size}) " +
                        "because some parent BPN-L/S entries are missing!"
            }
        }
        return poolClient.addresses.createAddresses(createRequests)
            .also { logger.info { "Pool accepted ${it.entityCount} new addresses, ${it.errorCount} were refused" } }
    }

    private fun isSiteMainAddress(it: GateAddressInfo): Boolean {

        val mainAdressExternalId = it.siteExternalId?.let { it1 -> gateClient.sites.getSiteByExternalId(it1).mainAddress.externalId }
        return it.externalId == mainAdressExternalId
    }

    private fun isLegalAddress(it: GateAddressInfo): Boolean {

        val legalAdressExternalId =
            it.legalEntityExternalId?.let { it1 -> gateClient.legalEntities.getLegalEntityByExternalId(it1).legalAddress.externalId }
        return it.externalId == legalAdressExternalId
    }

    fun updateAddressesInPool(entriesToUpdate: Collection<GateAddressInfo>): AddressPartnerUpdateResponseWrapper {
        val updateRequests = entriesToUpdate.map {
            AddressPartnerUpdateRequest(
                address = LogisticAddress(
                    name = it.address.nameParts.firstOrNull(),
                    states = it.address.states.map(::gateToPoolAddressState),
                    identifiers = it.address.identifiers.map(::gateToPoolAddressIdentifier),
                    physicalPostalAddress = gateToPoolPhysicalAddress(it.address.physicalPostalAddress),
                    alternativePostalAddress = it.address.alternativePostalAddress?.let(::gateToPoolAlternativeAddress),
                    confidenceCriteria = dummyConfidenceCriteria
                ),
                bpna = it.bpn!!
            )
        }

        return poolClient.addresses.updateAddresses(updateRequests)
            .also { logger.info { "Pool accepted ${it.entityCount} updated addresses, ${it.errorCount} were refused" } }
    }
}
