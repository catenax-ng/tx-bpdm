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

package org.eclipse.tractusx.bpdm.common.dto

import io.swagger.v3.oas.annotations.media.ArraySchema
import io.swagger.v3.oas.annotations.media.Schema

@Schema(name = "LogisticAddressDto", description = "Address record for a business partner")
data class LogisticAddressDto(

    @Schema(description = "Name of the logistic address of the business partner. This is notaccording to official\n" +
            "registers but according to the name the uploading sharing member chooses.")
    val addressName: String? = null,

    @ArraySchema(arraySchema = Schema(description = "Indicates if the LogisticAddress is \"Active\" or \"Inactive\"."))
    val status: Collection<AddressStateDto> = emptyList(),

    @Schema(description = "This indicates if the given BPNA is the main address to a site")
    val isSiteMainAddress: Boolean?,

    @ArraySchema(arraySchema = Schema(description = "List of identifiers"))
    val addressIdentifier: Collection<AddressIdentifierDto> = emptyList(),

    @Schema(description = "Postal address")
    val postalAddress: PostalAddressDto

)