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

package org.eclipse.tractusx.bpdm.gate.controller

import org.eclipse.tractusx.bpdm.common.dto.PageDto
import org.eclipse.tractusx.bpdm.common.dto.PaginationRequest
import org.eclipse.tractusx.bpdm.common.service.toPageRequest
import org.eclipse.tractusx.bpdm.gate.api.GateBusinessPartnerApi
import org.eclipse.tractusx.bpdm.gate.api.model.request.BusinessPartnerInputRequest
import org.eclipse.tractusx.bpdm.gate.api.model.response.BusinessPartnerInputResponse
import org.eclipse.tractusx.bpdm.gate.api.model.response.BusinessPartnerOutputResponse
import org.eclipse.tractusx.bpdm.gate.config.ApiConfigProperties
import org.eclipse.tractusx.bpdm.gate.config.PermissionConfigProperties
import org.eclipse.tractusx.bpdm.gate.service.BusinessPartnerService
import org.eclipse.tractusx.bpdm.gate.util.containsDuplicates
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.RestController

@RestController
class BusinessPartnerController(
    val businessPartnerService: BusinessPartnerService,
    val apiConfigProperties: ApiConfigProperties
) : GateBusinessPartnerApi {

    @PreAuthorize("hasAuthority(${PermissionConfigProperties.WRITE_INPUT_AUTHORITY})")
    override fun upsertBusinessPartnersInput(businessPartners: Collection<BusinessPartnerInputRequest>): ResponseEntity<Collection<BusinessPartnerInputResponse>> {
        if (businessPartners.size > apiConfigProperties.upsertLimit || businessPartners.map { it.externalId }.containsDuplicates()) {
            return ResponseEntity(HttpStatus.BAD_REQUEST)
        }
        val result = businessPartnerService.upsertBusinessPartnersInput(businessPartners.toList())
        return ResponseEntity.ok(result)
    }

    @PreAuthorize("hasAuthority(${PermissionConfigProperties.READ_INPUT_AUTHORITY})")
    override fun getBusinessPartnersInput(
        externalIds: Collection<String>?,
        paginationRequest: PaginationRequest
    ): PageDto<BusinessPartnerInputResponse> {
        return businessPartnerService.getBusinessPartnersInput(paginationRequest.toPageRequest(), externalIds)
    }

    @PreAuthorize("hasAuthority(${PermissionConfigProperties.READ_OUTPUT_AUTHORITY})")
    override fun getBusinessPartnersOutput(
        externalIds: Collection<String>?,
        paginationRequest: PaginationRequest
    ): PageDto<BusinessPartnerOutputResponse> {
        return businessPartnerService.getBusinessPartnersOutput(paginationRequest.toPageRequest(), externalIds)
    }
}
