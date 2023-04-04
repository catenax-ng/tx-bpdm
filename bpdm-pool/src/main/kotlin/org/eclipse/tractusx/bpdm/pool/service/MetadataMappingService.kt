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

import org.eclipse.tractusx.bpdm.common.dto.LegalEntityDto
import org.eclipse.tractusx.bpdm.common.exception.BpdmMultipleNotfound
import org.eclipse.tractusx.bpdm.pool.dto.MetadataMappingDto
import org.eclipse.tractusx.bpdm.pool.entity.IdentifierType
import org.eclipse.tractusx.bpdm.pool.entity.LegalForm
import org.eclipse.tractusx.bpdm.pool.repository.IdentifierTypeRepository
import org.eclipse.tractusx.bpdm.pool.repository.LegalFormRepository
import org.springframework.stereotype.Service

/**
 * Service for fetching and mapping metadata entities referenced by [LegalEntityDto]
 */
@Service
class MetadataMappingService(
    private val identifierTypeRepository: IdentifierTypeRepository,
    private val legalFormRepository: LegalFormRepository
) {

    /**
     * Fetch metadata entities referenced in [partners] and map them by their referenced keys
     */
    fun mapRequests(partners: Collection<LegalEntityDto>): MetadataMappingDto {
        return MetadataMappingDto(
            idTypes = mapIdentifierTypes(partners),
            legalForms = mapLegalForms(partners)
        )
    }

    /**
     * Fetch [IdentifierType] referenced in [partners] and map them by their referenced keys
     */
    fun mapIdentifierTypes(partners: Collection<LegalEntityDto>): Map<String, IdentifierType>{
        return mapIdentifierTypes(partners.flatMap { it.identifiers.map { id -> id.type } }.toSet())
    }

    /**
     * Fetch [LegalForm] referenced in [partners] and map them by their referenced keys
     */
    fun mapLegalForms(partners: Collection<LegalEntityDto>): Map<String, LegalForm>{
        return mapLegalForms(partners.mapNotNull { it.legalForm }.toSet())
    }


    private fun mapIdentifierTypes(keys: Set<String>): Map<String, IdentifierType>{
        val typeMap = identifierTypeRepository.findByTechnicalKeyIn(keys).associateBy { it.technicalKey }
        assertKeysFound(keys, typeMap)
        return typeMap
    }

    private fun mapLegalForms(keys: Set<String>): Map<String, LegalForm>{
        val typeMap = legalFormRepository.findByTechnicalKeyIn(keys).associateBy { it.technicalKey }
        assertKeysFound(keys, typeMap)
        return typeMap
    }

    private inline fun <reified T> assertKeysFound(keys: Set<String>, typeMap: Map<String, T>){
        val keysNotfound = keys.minus(typeMap.keys)
        if(keysNotfound.isNotEmpty()) throw BpdmMultipleNotfound(T::class.simpleName!!, keysNotfound )
    }

}