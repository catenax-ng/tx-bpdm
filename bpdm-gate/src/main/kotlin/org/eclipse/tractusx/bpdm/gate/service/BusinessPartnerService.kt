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

import org.eclipse.tractusx.bpdm.common.dto.AddressType
import org.eclipse.tractusx.bpdm.common.dto.BusinessPartnerType
import org.eclipse.tractusx.bpdm.common.dto.response.PageDto
import org.eclipse.tractusx.bpdm.common.model.StageType
import org.eclipse.tractusx.bpdm.common.service.toPageDto
import org.eclipse.tractusx.bpdm.gate.api.model.ChangelogType
import org.eclipse.tractusx.bpdm.gate.api.model.IBaseBusinessPartnerGateDto
import org.eclipse.tractusx.bpdm.gate.api.model.request.BusinessPartnerInputRequest
import org.eclipse.tractusx.bpdm.gate.api.model.request.BusinessPartnerOutputRequest
import org.eclipse.tractusx.bpdm.gate.api.model.response.BusinessPartnerInputDto
import org.eclipse.tractusx.bpdm.gate.api.model.response.BusinessPartnerOutputDto
import org.eclipse.tractusx.bpdm.gate.api.model.response.SharingStateDto
import org.eclipse.tractusx.bpdm.gate.entity.ChangelogEntry
import org.eclipse.tractusx.bpdm.gate.entity.generic.BusinessPartner
import org.eclipse.tractusx.bpdm.gate.repository.ChangelogRepository
import org.eclipse.tractusx.bpdm.gate.repository.generic.BusinessPartnerRepository
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class BusinessPartnerService(
    private val businessPartnerRepository: BusinessPartnerRepository,
    private val businessPartnerMappings: BusinessPartnerMappings,
    private val sharingStateService: SharingStateService,
    private val changelogRepository: ChangelogRepository
) {

    @Transactional
    fun upsertBusinessPartnersInput(dtos: Collection<BusinessPartnerInputRequest>): Collection<BusinessPartnerInputDto> {
        val existingEntitiesByExternalId = getExistingEntityByExternalId(dtos, StageType.Input)

        val savedEntities = dtos.map { dto ->
            existingEntitiesByExternalId[dto.externalId]
                ?.let { existingEntity -> updateBusinessPartnerInput(existingEntity, dto) }
                ?: run { insertBusinessPartnerInput(dto) }
        }

        return savedEntities.map(businessPartnerMappings::toBusinessPartnerInputDto)
    }

    private fun insertBusinessPartnerInput(dto: BusinessPartnerInputRequest): BusinessPartner {
        val newEntity = businessPartnerMappings.toBusinessPartner(dto, StageType.Input)
        return businessPartnerRepository.save(newEntity)
            .also {
                saveChangelog(dto.externalId, ChangelogType.CREATE, StageType.Input, checkBusinessPartnerType(dto.postalAddress.addressType))
                initSharingState(dto)
            }
    }

    private fun updateBusinessPartnerInput(existingEntity: BusinessPartner, dto: BusinessPartnerInputRequest): BusinessPartner {
        businessPartnerMappings.updateBusinessPartner(existingEntity, dto)
        return businessPartnerRepository.save(existingEntity)
            .also {
                saveChangelog(dto.externalId, ChangelogType.UPDATE, StageType.Input, checkBusinessPartnerType(dto.postalAddress.addressType))
            }
    }

    private fun checkBusinessPartnerType(type: AddressType?): List<BusinessPartnerType>? {
        return when (type) {
            AddressType.LegalAndSiteMainAddress -> listOf(BusinessPartnerType.LEGAL_ENTITY, BusinessPartnerType.SITE)
            AddressType.AdditionalAddress -> listOf(BusinessPartnerType.ADDRESS)
            AddressType.LegalAddress -> listOf(BusinessPartnerType.LEGAL_ENTITY)
            AddressType.SiteMainAddress -> listOf(BusinessPartnerType.SITE)
            else -> null
        }
    }

    //Output Logic
    @Transactional
    fun upsertBusinessPartnersOutput(dtos: Collection<BusinessPartnerOutputRequest>): Collection<BusinessPartnerInputDto> {
        val existingEntitiesByExternalId = getExistingEntityByExternalId(dtos, StageType.Output)

        val savedEntities = dtos.map { dto ->
            existingEntitiesByExternalId[dto.externalId]
                ?.let { existingEntity -> updateBusinessPartnerOutput(existingEntity, dto) }
                ?: run { insertBusinessPartnerOutput(dto) }
        }

        return savedEntities.map(businessPartnerMappings::toBusinessPartnerInputDto)
    }

    private fun insertBusinessPartnerOutput(dto: BusinessPartnerOutputRequest): BusinessPartner {
        val newEntity = businessPartnerMappings.toBusinessPartnerOutput(dto, StageType.Output)
        return businessPartnerRepository.save(newEntity)
            .also {
                saveChangelog(dto.externalId, ChangelogType.CREATE, StageType.Output, checkBusinessPartnerType(dto.postalAddress.addressType))
                initSharingState(dto)
            }
    }

    private fun updateBusinessPartnerOutput(existingEntity: BusinessPartner, dto: BusinessPartnerOutputRequest): BusinessPartner {
        businessPartnerMappings.updateBusinessPartnerOutput(existingEntity, dto)
        return businessPartnerRepository.save(existingEntity)
            .also {
                saveChangelog(dto.externalId, ChangelogType.UPDATE, StageType.Output, checkBusinessPartnerType(dto.postalAddress.addressType))
            }
    }

    fun getBusinessPartnersInput(pageRequest: PageRequest, externalIds: Collection<String>?): PageDto<BusinessPartnerInputDto> {
        val stage = StageType.Input
        val page = when {
            externalIds.isNullOrEmpty() -> businessPartnerRepository.findByStage(stage, pageRequest)
            else -> businessPartnerRepository.findByStageAndExternalIdIn(stage, externalIds, pageRequest)
        }
        return page.toPageDto(businessPartnerMappings::toBusinessPartnerInputDto)
    }

    fun getBusinessPartnersOutput(pageRequest: PageRequest, externalIds: Collection<String>?): PageDto<BusinessPartnerOutputDto> {
        val stage = StageType.Output
        val page = when {
            externalIds.isNullOrEmpty() -> businessPartnerRepository.findByStage(stage, pageRequest)
            else -> businessPartnerRepository.findByStageAndExternalIdIn(stage, externalIds, pageRequest)
        }
        return page.toPageDto(businessPartnerMappings::toBusinessPartnerOutputDto)
    }

    private fun getExistingEntityByExternalId(
        dtos: Collection<IBaseBusinessPartnerGateDto>,
        stage: StageType
    ): Map<String, BusinessPartner> {
        val externalIds = dtos.map { it.externalId }.toSet()
        return businessPartnerRepository.findByStageAndExternalIdIn(stage, externalIds)
            .associateBy { it.externalId }
    }

    private fun initSharingState(dto: IBaseBusinessPartnerGateDto) {
        // TODO make businessPartnerType optional
        sharingStateService.upsertSharingState(SharingStateDto(BusinessPartnerType.ADDRESS, dto.externalId))
    }

    private fun saveChangelog(externalId: String, changelogType: ChangelogType, stage: StageType, businessPartnerType: List<BusinessPartnerType>?) {
        // TODO make businessPartnerType optional

        businessPartnerType?.forEach { type ->
            changelogRepository.save(ChangelogEntry(externalId, type, changelogType, stage))
        } ?: changelogRepository.save(ChangelogEntry(externalId, null, changelogType, stage))
    }
}
