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

import com.neovisionaries.i18n.CountryCode
import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "Country subdivision")
data class CountrySubdivision(

    @get:Schema(description = "Country code")
    val countryCode: CountryCode,

    @get:Schema(description = "The country subdivision code according to ISO 3166-2")
    val code: String,

    @get:Schema(description = "The name of the country subdivision according to ISO 3166-2")
    val name: String
)
