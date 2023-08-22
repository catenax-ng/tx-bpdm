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

package org.eclipse.tractusx.bpdm.gate.api.config

import org.eclipse.tractusx.bpdm.common.config.AppInfoProperties
import org.eclipse.tractusx.bpdm.common.config.OpenApiConfig
import org.eclipse.tractusx.bpdm.common.config.SecurityConfigProperties
import org.eclipse.tractusx.bpdm.common.dto.openapidescription.LogisticAddressDescription
import org.eclipse.tractusx.bpdm.gate.api.model.AddressGateOutputChildRequest
import org.eclipse.tractusx.bpdm.gate.api.model.LogisticAddressGateDto
import org.eclipse.tractusx.bpdm.gate.api.model.response.AddressGateInputDto
import org.eclipse.tractusx.bpdm.gate.api.model.response.AddressGateOutputDto
import org.springframework.context.annotation.Configuration

@Configuration
class OpenApiGateConfig(
    override val securityProperties: SecurityConfigProperties,
    override val infoProperties: AppInfoProperties
) : OpenApiConfig() {

    override fun getSchemaOverrides(): Collection<SchemaOverrideInfo> {
        return super.getSchemaOverrides()
            .union(
                setOf(
                    // Schema copies with alternative description
                    schemaOverride<AddressGateInputDto>(
                        LogisticAddressDescription.legalAddress,
                        LogisticAddressDescription.legalAddressAliasForAddressGateInputDto
                    ),
                    schemaOverride<AddressGateOutputDto>(
                        LogisticAddressDescription.legalAddress,
                        LogisticAddressDescription.legalAddressAliasForAddressGateOutputDto
                    ),
                    schemaOverride<AddressGateOutputChildRequest>(
                        LogisticAddressDescription.legalAddress,
                        LogisticAddressDescription.legalAddressAliasForAddressGateOutputChildRequest
                    ),
                    schemaOverride<LogisticAddressGateDto>(
                        LogisticAddressDescription.legalAddress,
                        LogisticAddressDescription.legalAddressAliasForLogisticAddressGateDto
                    )
                )
            )
    }

}