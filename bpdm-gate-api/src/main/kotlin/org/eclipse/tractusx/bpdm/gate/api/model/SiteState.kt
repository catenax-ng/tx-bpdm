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

package org.eclipse.tractusx.bpdm.gate.api.model

import io.swagger.v3.oas.annotations.media.Schema
import org.eclipse.tractusx.bpdm.common.dto.ISiteState
import org.eclipse.tractusx.bpdm.common.dto.openapidescription.SiteStateDescription
import org.eclipse.tractusx.bpdm.common.model.BusinessStateType
import java.time.LocalDateTime

@Schema(description = SiteStateDescription.header)
data class SiteState(

    val description: String?,
    override val validFrom: LocalDateTime?,
    override val validTo: LocalDateTime?,
    override val type: BusinessStateType

) : ISiteState
