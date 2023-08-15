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
import org.eclipse.tractusx.bpdm.common.dto.openapidescription.LogisticAddressDescription


@Schema(description = "Address record for a business partner")
data class LogisticAddressDto(
    @get:Schema(description = LogisticAddressDescription.name)
    val name: String? = null,

    @get:ArraySchema(arraySchema = Schema(description = LogisticAddressDescription.states))
    val states: Collection<AddressStateDto> = emptyList(),

    @get:ArraySchema(arraySchema = Schema(description = LogisticAddressDescription.identifiers))
    val identifiers: Collection<AddressIdentifierDto> = emptyList(),

    // TODO OpenAPI description for complex field does not work!!
    @get:Schema(description = LogisticAddressDescription.physicalPostalAddress)
    val physicalPostalAddress: PhysicalPostalAddressDto,

    // TODO OpenAPI description for complex field does not work!!
    @get:Schema(description = LogisticAddressDescription.alternativePostalAddress)
    val alternativePostalAddress: AlternativePostalAddressDto? = null
)