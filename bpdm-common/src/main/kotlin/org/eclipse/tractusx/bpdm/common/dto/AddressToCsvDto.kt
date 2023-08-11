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

import com.opencsv.bean.CsvBindByName

data class AddressToCsvDto(
    @CsvBindByName(column = "External ID of additional address")
    val addressExternalId: String? = null,

    @CsvBindByName(column = "Legal Entity External ID")
    val legalEntityExternalId: String? = null,

    @CsvBindByName(column = "Site External ID")
    val siteExternalId: String? = null,

    @CsvBindByName(column = "Country")
    val country: String? = null,

    @CsvBindByName(column = "City")
    val city: String? = null,

    @CsvBindByName(column = "Street")
    val street: String? = null,

    @CsvBindByName(column = "Name of address")
    val addressName: String? = null,

    @CsvBindByName(column = "Postal Code")
    val postalCode: String? = null,

    @CsvBindByName(column = "House number")
    val houseNumber: String? = null,

    @CsvBindByName(column = "Street name prefix")
    val streetNamePrefix: String? = null,

    @CsvBindByName(column = "Additional street name prefix")
    val additionalStreetNamePrefix: String? = null,

    @CsvBindByName(column = "Street name suffix")
    val streetNameSuffix: String? = null,

    @CsvBindByName(column = "Additional street name suffix")
    val additionalStreetNameSuffix: String? = null,

    @CsvBindByName(column = "Milestone")
    val milestone: String? = null,

    @CsvBindByName(column = "Direction")
    val direction: String? = null,

    @CsvBindByName(column = "Administrative Area Level 1")
    val adminAreaLevelOne: String? = null,

    @CsvBindByName(column = "Administrative Area Level 2")
    val adminAreaLevelTwo: String? = null,

    @CsvBindByName(column = "Administrative Area Level 3")
    val adminAreaLevelThree: String? = null,

    @CsvBindByName(column = "District")
    val district: String? = null,

    @CsvBindByName(column = "Company Postal Code")
    val companyPostalCode: String? = null,


    @CsvBindByName(column = "Industrial Zone")
    val industrialZone: String? = null,

    @CsvBindByName(column = "Building")
    val building: String? = null,


    @CsvBindByName(column = "Floor")
    val floor: String? = null,

    @CsvBindByName(column = "Door")
    val door: String? = null,

    @CsvBindByName(column = "Longitude")
    val longitude: String? = null,

    @CsvBindByName(column = "Latitude")
    val latitude: String? = null,

    @CsvBindByName(column = "Altitude")
    val altitude: String? = null,

    @CsvBindByName(column = "Alternative Postal Address Country")
    val altPostalAddressCountry: String? = null,

    @CsvBindByName(column = "Alternative Postal Address Postal Code")
    val altPostalAddressPostalCode: String? = null,

    @CsvBindByName(column = "Alternative Postal Address City")
    val altPostalAddressCity: String? = null,

    @CsvBindByName(column = "Alternative Postal Address Administrative Area Level 1")
    val altPostalAddressAdminAreaLevelOne: String? = null,

    @CsvBindByName(column = "Delivery Service Number")
    val deliveryServiceNumber: String? = null,

    @CsvBindByName(column = "Delivery Service Type")
    val deliveryServiceType: String? = null,

    @CsvBindByName(column = "Delivery Service Qualifier")
    val deliveryServiceQualifier: String? = null,

    @CsvBindByName(column = "Longitude")
    val altPostalAddressLongitude: String? = null,

    @CsvBindByName(column = "Latitude")
    val altPostalAddressLatitude: String? = null,

    @CsvBindByName(column = "Altitude")
    val altPostalAddressAltitude: String? = null,

    @CsvBindByName(column = "Address Description")
    val addressDescription: String? = null,

    @CsvBindByName(column = "Address Valid From")
    val addressValidFrom: String? = null,

    @CsvBindByName(column = "Address Valid To")
    val addressValidTo: String? = null,

    @CsvBindByName(column = "Address Status")
    val addressStatus: String? = null,

    @CsvBindByName(column = "Address Identifier")
    val addressIdentifier: String? = null,

    @CsvBindByName(column = "Address Identifier Type")
    val addressIdentifierType: String? = null,

    ) : BusinessPartnersToCsv()