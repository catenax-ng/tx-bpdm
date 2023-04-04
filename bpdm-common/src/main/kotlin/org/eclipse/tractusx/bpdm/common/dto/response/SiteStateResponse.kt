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

package org.eclipse.tractusx.bpdm.common.dto.response

import io.swagger.v3.oas.annotations.media.Schema
import org.eclipse.tractusx.bpdm.common.dto.response.type.TypeKeyNameDto
import org.eclipse.tractusx.bpdm.common.model.BusinessStateType
import java.time.LocalDateTime

@Schema(name = "SiteStateResponse", description = "Status record of a site")
data class SiteStateResponse(
    @Schema(description = "Description of the status")
    val description: String?,

    @Schema(description = "Since when the status is/was valid")
    val validFrom: LocalDateTime?,

    @Schema(description = "Until the status was valid, if applicable")
    val validTo: LocalDateTime?,

    @Schema(description = "The type of this status")
    val type: TypeKeyNameDto<BusinessStateType>
)