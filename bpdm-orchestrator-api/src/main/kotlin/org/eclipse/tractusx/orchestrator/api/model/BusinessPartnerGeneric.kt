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

package org.eclipse.tractusx.orchestrator.api.model

import io.swagger.v3.oas.annotations.media.Schema
import org.eclipse.tractusx.bpdm.common.dto.*


@Schema(
    description = "Generic business partner with external id"
)
data class BusinessPartnerGeneric(
    override val nameParts: List<String> = emptyList(),
    override val identifiers: Collection<BusinessPartnerIdentifier> = emptyList(),
    override val states: Collection<BusinessPartnerState> = emptyList(),
    override val roles: Collection<BusinessPartnerRole> = emptyList(),
    override val legalEntity: LegalEntityRepresentation = LegalEntityRepresentation(),
    override val site: SiteRepresentation = SiteRepresentation(),
    override val address: AddressRepresentation = AddressRepresentation(),
    @get:Schema(description = "The BPNL of the company sharing and claiming this business partner as its own")
    val ownerBpnL: String? = null
) : IBaseBusinessPartner

data class LegalEntityRepresentation(
    override val legalEntityBpn: String? = null,
    override val legalName: String? = null,
    override val shortName: String? = null,
    override val legalForm: String? = null,
    override val classifications: Collection<BusinessPartnerClassification> = emptyList(),
    val confidenceCriteria: ConfidenceCriteria? = null
) : IBaseLegalEntityRepresentation

data class SiteRepresentation(
    override val siteBpn: String? = null,
    override val name: String? = null,
    val confidenceCriteria: ConfidenceCriteria? = null
) : IBaseSiteRepresentation

data class AddressRepresentation(
    override val addressBpn: String? = null,
    override val name: String? = null,
    override val addressType: AddressType? = null,
    override val physicalPostalAddress: PhysicalPostalAddress? = null,
    override val alternativePostalAddress: AlternativePostalAddress? = null,
    val confidenceCriteria: ConfidenceCriteria? = null
) : IBaseAddressRepresentation


