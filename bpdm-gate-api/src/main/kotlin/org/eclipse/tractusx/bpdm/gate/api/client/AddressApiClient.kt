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

package org.eclipse.tractusx.bpdm.gate.api.client

import io.swagger.v3.oas.annotations.Parameter
import jakarta.validation.Valid
import org.eclipse.tractusx.bpdm.common.dto.PageDto
import org.eclipse.tractusx.bpdm.common.dto.PaginationRequest
import org.eclipse.tractusx.bpdm.gate.api.GateAddressApi
import org.eclipse.tractusx.bpdm.gate.api.model.request.AddressGateInputRequest
import org.eclipse.tractusx.bpdm.gate.api.model.request.AddressGateOutputRequest
import org.eclipse.tractusx.bpdm.gate.api.model.response.AddressGateInputResponse
import org.eclipse.tractusx.bpdm.gate.api.model.response.AddressGateOutputResponse
import org.springdoc.core.annotations.ParameterObject
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.service.annotation.GetExchange
import org.springframework.web.service.annotation.HttpExchange
import org.springframework.web.service.annotation.PostExchange
import org.springframework.web.service.annotation.PutExchange

@HttpExchange("/api/catena")
interface AddressApiClient : GateAddressApi {

    @GetExchange("/input/addresses/{externalId}")
    override fun getAddressByExternalId(@Parameter(description = "External ID") @PathVariable externalId: String): AddressGateInputResponse

    @GetExchange("/input/addresses")
    override fun getAddresses(
        @ParameterObject @Valid paginationRequest: PaginationRequest
    ): PageDto<AddressGateInputResponse>

    @PostExchange("/input/addresses/search")
    override fun getAddressesByExternalIds(
        @ParameterObject @Valid paginationRequest: PaginationRequest,
        @RequestBody externalIds: Collection<String>
    ): PageDto<AddressGateInputResponse>

    @PostExchange("/output/addresses/search")
    override fun getAddressesOutput(
        @ParameterObject @Valid paginationRequest: PaginationRequest,
        @RequestBody(required = false) externalIds: Collection<String>?
    ): PageDto<AddressGateOutputResponse>

    @PutExchange("/input/addresses")
    override fun upsertAddresses(
        @RequestBody addresses: Collection<AddressGateInputRequest>
    ): ResponseEntity<Unit>

    @PutExchange("/output/addresses")
    override fun upsertAddressesOutput(
        @RequestBody addresses: Collection<AddressGateOutputRequest>
    ): ResponseEntity<Unit>

}