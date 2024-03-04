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

import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.neovisionaries.i18n.CountryCode
import io.swagger.v3.oas.annotations.media.Schema
import org.eclipse.tractusx.bpdm.common.dto.GeoCoordinate
import org.eclipse.tractusx.bpdm.common.dto.IBasePhysicalPostalAddress
import org.eclipse.tractusx.bpdm.common.dto.openapidescription.PostalAddressDescription
import org.eclipse.tractusx.bpdm.common.service.DataClassUnwrappedJsonDeserializer

@JsonDeserialize(using = DataClassUnwrappedJsonDeserializer::class)
@Schema(description = PostalAddressDescription.headerPhysical)
data class PhysicalPostalAddress(

    override val geographicCoordinates: GeoCoordinate?,
    override val country: CountryCode,
    override val administrativeAreaLevel1: String?,
    override val administrativeAreaLevel2: String?,
    override val administrativeAreaLevel3: String?,
    override val postalCode: String?,
    override val city: String,
    override val district: String?,
    override val street: Street?,
    override val companyPostalCode: String?,
    override val industrialZone: String?,
    override val building: String?,
    override val floor: String?,
    override val door: String?

) : IBasePhysicalPostalAddress
