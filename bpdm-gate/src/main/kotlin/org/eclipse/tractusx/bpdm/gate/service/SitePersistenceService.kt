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

import org.eclipse.tractusx.bpdm.gate.dto.SiteGateInputRequest
import org.eclipse.tractusx.bpdm.gate.entity.SiteGate
import org.eclipse.tractusx.bpdm.gate.repository.GateSiteRepository
import org.springframework.stereotype.Service

@Service
class SitePersistenceService(private val gateSiteRepository: GateSiteRepository) {

    fun persistAddressBP(sites: Collection<SiteGateInputRequest>) {

        val externalIdColl: MutableCollection<String> = mutableListOf()
        sites.forEach { externalIdColl.add(it.externalId) }

        val addressRecord = gateSiteRepository.findByExternalIdIn(externalIdColl)

        sites.forEach { site ->
            val fullAddress = site.toSiteGate()
            addressRecord.find { it.externalId == site.externalId }?.let { existingAddress ->
                updateAddress(existingAddress, site)
                gateSiteRepository.save(existingAddress)
            } ?: run {
                gateSiteRepository.save(fullAddress)
            }
        }
    }

    private fun updateAddress(site: SiteGate, sitesRequest: SiteGateInputRequest) {

        site.bpn = sitesRequest.bpn.toString()
        site.name = sitesRequest.site.name
        site.legalEntityExternalId = sitesRequest.legalEntityExternalId
        site.mainAddress = sitesRequest.site.mainAddress.toAddressGateDto()

    }

}