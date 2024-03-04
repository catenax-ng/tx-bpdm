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

package org.eclipse.tractusx.bpdm.pool.api.model

import io.swagger.v3.oas.annotations.media.Schema
import org.eclipse.tractusx.bpdm.common.dto.AddressType
import org.eclipse.tractusx.bpdm.common.dto.IBaseLogisticAddress
import org.eclipse.tractusx.bpdm.common.dto.openapidescription.CommonDescription
import org.eclipse.tractusx.bpdm.common.dto.openapidescription.LogisticAddressDescription
import java.time.Instant

@Schema(description = LogisticAddressDescription.header)
data class LogisticAddressVerbose(

    @get:Schema(description = LogisticAddressDescription.bpna)
    val bpna: String,

    @get:Schema(description = LogisticAddressDescription.name)
    val name: String? = null,

    override val states: Collection<AddressStateVerbose> = emptyList(),
    override val identifiers: Collection<AddressIdentifierVerbose> = emptyList(),
    override val physicalPostalAddress: PhysicalPostalAddressVerbose,
    override val alternativePostalAddress: AlternativePostalAddressVerboseDto? = null,

    @get:Schema(description = LogisticAddressDescription.bpnLegalEntity)
    val bpnLegalEntity: String?,

    @get:Schema(name = "isLegalAddress", description = LogisticAddressDescription.isLegalAddress)
    val isLegalAddress: Boolean = false,

    @get:Schema(description = LogisticAddressDescription.bpnSite)
    val bpnSite: String?,

    @get:Schema(name = "isMainAddress", description = LogisticAddressDescription.isMainAddress)
    val isMainAddress: Boolean = false,

    @get:Schema(description = CommonDescription.createdAt)
    val createdAt: Instant,

    @get:Schema(description = CommonDescription.updatedAt)
    val updatedAt: Instant,

    override val confidenceCriteria: ConfidenceCriteria,

    @get:Schema(name = "addressType", description = LogisticAddressDescription.addressType)
    val addressType: AddressType? = null,
) : IBaseLogisticAddress
