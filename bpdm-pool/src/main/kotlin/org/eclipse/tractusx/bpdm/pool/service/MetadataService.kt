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

package org.eclipse.tractusx.bpdm.pool.service

import com.neovisionaries.i18n.CountryCode
import mu.KotlinLogging
import org.eclipse.tractusx.bpdm.common.dto.FieldQualityRuleDto
import org.eclipse.tractusx.bpdm.common.dto.IdentifierLsaType
import org.eclipse.tractusx.bpdm.common.dto.IdentifierTypeDto
import org.eclipse.tractusx.bpdm.common.dto.QualityLevel
import org.eclipse.tractusx.bpdm.common.dto.response.LegalFormDto
import org.eclipse.tractusx.bpdm.common.dto.response.PageDto
import org.eclipse.tractusx.bpdm.pool.api.model.request.LegalFormRequest
import org.eclipse.tractusx.bpdm.pool.entity.FieldQualityRule
import org.eclipse.tractusx.bpdm.pool.entity.IdentifierType
import org.eclipse.tractusx.bpdm.pool.entity.IdentifierTypeDetail
import org.eclipse.tractusx.bpdm.pool.entity.LegalForm
import org.eclipse.tractusx.bpdm.pool.exception.BpdmAlreadyExists
import org.eclipse.tractusx.bpdm.pool.repository.FieldQualityRuleRepository
import org.eclipse.tractusx.bpdm.pool.repository.IdentifierTypeRepository
import org.eclipse.tractusx.bpdm.pool.repository.LegalFormRepository
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.domain.Specification
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

/**
 * Service for fetching and creating metadata entities
 */
@Service
class MetadataService(
    val identifierTypeRepository: IdentifierTypeRepository,
    val legalFormRepository: LegalFormRepository,
    val fieldQualityRuleRepository: FieldQualityRuleRepository
) {

    private val logger = KotlinLogging.logger { }

    @Transactional
    fun createIdentifierType(type: IdentifierTypeDto): IdentifierTypeDto {
        if (identifierTypeRepository.findByLsaTypeAndTechnicalKey(type.lsaType, type.technicalKey) != null)
            throw BpdmAlreadyExists(IdentifierType::class.simpleName!!, "${type.technicalKey}/${type.lsaType}")

        logger.info { "Create new Identifier-Type with key ${type.technicalKey}, lsaType ${type.lsaType} and name ${type.name}" }
        val entity = IdentifierType(
            technicalKey = type.technicalKey,
            lsaType = type.lsaType,
            name = type.name
        )
        entity.details.addAll(
            type.details.map { IdentifierTypeDetail(entity, it.country, it.mandatory) }.toSet()
        )
        return identifierTypeRepository.save(entity).toDto()
    }

    fun getIdentifierTypes(pageRequest: Pageable, lsaType: IdentifierLsaType, country: CountryCode? = null): PageDto<IdentifierTypeDto> {
        val spec = Specification.allOf(
            IdentifierTypeRepository.Specs.byLsaType(lsaType),
            IdentifierTypeRepository.Specs.byCountry(country)
        )
        val page = identifierTypeRepository.findAll(spec, pageRequest)
        return page.toDto(page.content.map { it.toDto() })
    }

    @Transactional
    fun createLegalForm(request: LegalFormRequest): LegalFormDto {
        if (legalFormRepository.findByTechnicalKey(request.technicalKey) != null)
            throw BpdmAlreadyExists(LegalForm::class.simpleName!!, request.technicalKey)

        logger.info { "Create new Legal-Form with key ${request.technicalKey} and name ${request.name}" }

        val legalForm = LegalForm(
            technicalKey = request.technicalKey,
            name = request.name,
            abbreviation = request.abbreviation
        )

        return legalFormRepository.save(legalForm).toDto()
    }

    fun getLegalForms(pageRequest: Pageable): PageDto<LegalFormDto> {
        val page = legalFormRepository.findAll(pageRequest)
        return page.toDto(page.content.map { it.toDto() })
    }

    /**
     * Get quality rules for the given country merged with the default rules. Forbidden rules are ignored.
     */
    fun getFieldQualityRules(country: CountryCode): Collection<FieldQualityRuleDto> {

        val defaultRules = fieldQualityRuleRepository.findByCountryCodeIsNullOrderBySchemaNameAscFieldPathAsc()
        val rulesForCountry = fieldQualityRuleRepository.findByCountryCodeOrderBySchemaNameAscFieldPathAsc(country)

        val pathToDefaultRule = defaultRules.associateBy(
            { it.schemaName + "." + it.fieldPath }, { it }
        )
        val pathToCountrRule = rulesForCountry.associateBy(
            { it.schemaName + "." + it.fieldPath }, { it }
        )

        val pathsFromDefaultAndCountry = pathToDefaultRule.keys + pathToCountrRule.keys

        val mergedRulesForCountry = pathsFromDefaultAndCountry.mapNotNull { path ->
            mergeRules(pathToDefaultRule[path], pathToCountrRule[path])
        }

        val resultList = mergedRulesForCountry.filter {
            it.qualityLevel != QualityLevel.FORBIDDEN
        }.map { rule ->
            FieldQualityRuleDto(
                fieldPath = rule.fieldPath,
                schemaName = rule.schemaName,
                country = (if (rule.countryCode != null) rule.countryCode else country)!!,
                qualityLevel = rule.qualityLevel
            )
        }

        resultList.sortedWith(compareBy({ it.schemaName }, { it.fieldPath }))
        return resultList
    }

    /**
     * If no country rule exists use default rules
     */
    private fun mergeRules(defaultRule: FieldQualityRule?, countryRule: FieldQualityRule?): FieldQualityRule? {

        if (countryRule == null) {
            return defaultRule
        }

        return countryRule
    }

}