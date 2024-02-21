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

package org.eclipse.tractusx.bpdm.gate.api

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import jakarta.validation.Valid
import org.eclipse.tractusx.bpdm.common.dto.PaginationRequest
import org.eclipse.tractusx.bpdm.gate.api.model.request.ChangelogSearchRequest
import org.eclipse.tractusx.bpdm.gate.api.model.response.ChangelogGateResponse
import org.eclipse.tractusx.bpdm.gate.api.model.response.PageChangeLogDto
import org.springdoc.core.annotations.ParameterObject
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.service.annotation.HttpExchange
import org.springframework.web.service.annotation.PostExchange

@RequestMapping("/api/catena", produces = [MediaType.APPLICATION_JSON_VALUE])
@HttpExchange("/api/catena")
interface GateChangelogApi {

    @Operation(
        summary = "Returns changelog entries for changes to the business partner input stage",
        description = "Returns changelog entries as of a specified timestamp from the input stage, " +
                "optionally filtered by timestamp, an array of external IDs and a business partner type."
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "The changelog entries for the specified parameters"),
            ApiResponse(responseCode = "400", description = "On malformed pagination request", content = [Content()]),
        ]
    )
    @PostMapping("/input/changelog/search")
    @PostExchange("/input/changelog/search")
    fun getInputChangelog(
        @ParameterObject @Valid paginationRequest: PaginationRequest,
        @RequestBody searchRequest: ChangelogSearchRequest
    ): PageChangeLogDto<ChangelogGateResponse>

    @Operation(
        summary = "Returns changelog entries for changes to the business partner output stage",
        description = "Returns changelog entries as of a specified timestamp from the output stage, " +
                "optionally filtered by timestamp, an array of external IDs and a business partner type."
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "The changelog entries for the specified parameters"),
            ApiResponse(responseCode = "400", description = "On malformed pagination request", content = [Content()]),
        ]
    )
    @PostMapping("/output/changelog/search")
    @PostExchange("/output/changelog/search")
    fun getOutputChangelog(
        @ParameterObject @Valid paginationRequest: PaginationRequest,
        @RequestBody searchRequest: ChangelogSearchRequest
    ): PageChangeLogDto<ChangelogGateResponse>
}
