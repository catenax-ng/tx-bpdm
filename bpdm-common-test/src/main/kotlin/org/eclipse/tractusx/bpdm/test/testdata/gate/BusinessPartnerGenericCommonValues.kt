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

package org.eclipse.tractusx.bpdm.test.testdata.gate

import com.neovisionaries.i18n.CountryCode
import org.eclipse.tractusx.bpdm.common.dto.AddressType
import org.eclipse.tractusx.bpdm.common.dto.BusinessPartnerRole
import org.eclipse.tractusx.bpdm.common.dto.GeoCoordinateDto
import org.eclipse.tractusx.bpdm.common.model.BusinessStateType
import org.eclipse.tractusx.bpdm.common.model.ClassificationType
import org.eclipse.tractusx.bpdm.common.model.DeliveryServiceType
import org.eclipse.tractusx.orchestrator.api.model.*
import java.time.LocalDateTime

object BusinessPartnerGenericCommonValues {

    //Business Partner with two entries in every collection
    val businessPartner1 = BusinessPartnerGenericDto(
        nameParts = listOf("part-cleaned-1", "name-cleaned-2"),
        identifiers = listOf(
            BusinessPartnerIdentifierDto(
                type = "identifier-type-1-cleaned",
                value = "identifier-value-1-cleaned",
                issuingBody = "issuingBody-1-cleaned"
            ),
            BusinessPartnerIdentifierDto(
                type = "identifier-type-2-cleaned",
                value = "identifier-value-2-cleaned",
                issuingBody = "issuingBody-2-cleaned"
            ),
        ),
        states = listOf(
            BusinessPartnerStateDto(
                validFrom = LocalDateTime.of(2020, 9, 22, 15, 50),
                validTo = LocalDateTime.of(2023, 10, 23, 16, 40),
                type = BusinessStateType.INACTIVE
            ),
            BusinessPartnerStateDto(
                validFrom = LocalDateTime.of(2000, 8, 21, 14, 30),
                validTo = LocalDateTime.of(2020, 9, 22, 15, 50),
                type = BusinessStateType.ACTIVE
            )
        ),
        roles = listOf(
            BusinessPartnerRole.CUSTOMER,
            BusinessPartnerRole.SUPPLIER
        ),
        ownerBpnL = "BPNL_CLEANED_VALUES",
        legalEntity = LegalEntityRepresentation(
            legalEntityBpn = "000000123AAA123",
            legalName = "legal-name-cleaned",
            shortName = "shot-name-cleaned",
            legalForm = "legal-form-cleaned",
            classifications = listOf(
                BusinessPartnerClassificationDto(
                    type = ClassificationType.NACE,
                    code = "code-1-cleaned",
                    value = "value-1-cleaned"
                ),
                BusinessPartnerClassificationDto(
                    type = ClassificationType.NAF,
                    code = "code-2-cleaned",
                    value = "value-2-cleaned"
                ),
            ),
            confidenceCriteria = ConfidenceCriteriaDto(
                sharedByOwner = true,
                checkedByExternalDataSource = true,
                numberOfBusinessPartners = 7,
                lastConfidenceCheckAt = LocalDateTime.of(2022, 4, 3, 2, 1),
                nextConfidenceCheckAt = LocalDateTime.of(2026, 4, 3, 2, 1),
                confidenceLevel = 1
            )
        ),
        site = SiteRepresentation(
            siteBpn = "000000123BBB222",
            name = "Site Name",
            confidenceCriteria = ConfidenceCriteriaDto(
                sharedByOwner = false,
                checkedByExternalDataSource = false,
                numberOfBusinessPartners = 8,
                lastConfidenceCheckAt = LocalDateTime.of(2023, 4, 3, 2, 1),
                nextConfidenceCheckAt = LocalDateTime.of(2024, 4, 3, 2, 1),
                confidenceLevel = 2
            )
        ),
        address = AddressRepresentation(
            addressBpn = "000000123CCC333",
            name = "Address Name",
            addressType = AddressType.AdditionalAddress,
            physicalPostalAddress = PhysicalPostalAddressDto(
                geographicCoordinates = GeoCoordinateDto(0.5f, 0.5f, 0.5f),
                country = CountryCode.PT,
                administrativeAreaLevel1 = "PT-PT",
                administrativeAreaLevel2 = "pt-admin-level-2-cleaned",
                administrativeAreaLevel3 = "pt-admin-level-3-cleaned",
                postalCode = "phys-postal-code-cleaned",
                city = "city",
                district = "district",
                street = StreetDto(
                    name = "name",
                    houseNumber = "house-number",
                    houseNumberSupplement = "house-number-supplement",
                    milestone = "milestone",
                    direction = "direction",
                    namePrefix = "name-prefix",
                    additionalNamePrefix = "add-name-prefix",
                    nameSuffix = "name-suffix",
                    additionalNameSuffix = "add-name-suffix"

                ),
                companyPostalCode = "comp-postal-code",
                industrialZone = "industrial-zone",
                building = "building",
                floor = "floor",
                door = "door"
            ),
            alternativePostalAddress = AlternativePostalAddressDto(
                geographicCoordinates = GeoCoordinateDto(0.6f, 0.6f, 0.6f),
                country = CountryCode.PT,
                administrativeAreaLevel1 = "PT-PT",
                postalCode = "postal-code-cleaned",
                city = "alt-city-cleaned",
                deliveryServiceNumber = "delivery-service-number-cleaned",
                deliveryServiceQualifier = "delivery-service-qualifier-cleaned",
                deliveryServiceType = DeliveryServiceType.PO_BOX
            ),
            confidenceCriteria = ConfidenceCriteriaDto(
                sharedByOwner = false,
                checkedByExternalDataSource = true,
                numberOfBusinessPartners = 4,
                lastConfidenceCheckAt = LocalDateTime.of(2020, 4, 3, 2, 1),
                nextConfidenceCheckAt = LocalDateTime.of(2028, 4, 3, 2, 1),
                confidenceLevel = 5
            )
        )
    )

    //Business Partner with single entry in every collection
    val businessPartner2 = BusinessPartnerGenericDto(
        nameParts = listOf("name-part-2"),
        identifiers = listOf(
            BusinessPartnerIdentifierDto(
                type = "identifier-type-2",
                value = "identifier-value-2",
                issuingBody = "issuingBody-2"
            )
        ),
        states = listOf(
            BusinessPartnerStateDto(
                validFrom = LocalDateTime.of(1988, 10, 4, 22, 30),
                validTo = LocalDateTime.of(2023, 1, 1, 10, 10),
                type = BusinessStateType.ACTIVE
            )
        ),
        roles = listOf(
            BusinessPartnerRole.CUSTOMER
        ),
        legalEntity = LegalEntityRepresentation(
            legalEntityBpn = "BPNLTEST-2",
            legalName = "legal-name-2",
            shortName = "shortname-2",
            legalForm = "legal-form-2",
            classifications = listOf(
                BusinessPartnerClassificationDto(
                    type = ClassificationType.SIC,
                    code = "code-2",
                    value = "value-2"
                )
            ),
            confidenceCriteria = ConfidenceCriteriaDto(
                sharedByOwner = true,
                checkedByExternalDataSource = true,
                numberOfBusinessPartners = 7,
                lastConfidenceCheckAt = LocalDateTime.of(2022, 4, 3, 2, 1),
                nextConfidenceCheckAt = LocalDateTime.of(2026, 4, 3, 2, 1),
                confidenceLevel = 1
            )
        ),
        site = SiteRepresentation(
            siteBpn = "BPNSTEST-2",
            name = "site name 2",
            confidenceCriteria = ConfidenceCriteriaDto(
                sharedByOwner = true,
                checkedByExternalDataSource = true,
                numberOfBusinessPartners = 7,
                lastConfidenceCheckAt = LocalDateTime.of(2022, 4, 3, 2, 1),
                nextConfidenceCheckAt = LocalDateTime.of(2026, 4, 3, 2, 1),
                confidenceLevel = 1
            )
        ),
        address = AddressRepresentation(
            addressBpn = "BPNATEST-2",
            name = "address name 2",
            addressType = AddressType.LegalAddress,
            physicalPostalAddress = PhysicalPostalAddressDto(
                geographicCoordinates = GeoCoordinateDto(0.4f, 0.4f, 0.4f),
                country = CountryCode.FR,
                administrativeAreaLevel1 = "FR-ARA",
                administrativeAreaLevel2 = "fr-admin-level-2",
                administrativeAreaLevel3 = "fr-admin-level-3",
                postalCode = "phys-postal-code-2",
                city = "city-2",
                district = "district-2",
                street = StreetDto(
                    name = "name-2",
                    houseNumber = "house-number-2",
                    houseNumberSupplement = "house-number-supplement-2",
                    milestone = "milestone-2",
                    direction = "direction-2",
                    namePrefix = "name-prefix-2",
                    additionalNamePrefix = "add-name-prefix-2",
                    nameSuffix = "name-suffix-2",
                    additionalNameSuffix = "add-name-suffix-2"

                ),
                companyPostalCode = "comp-postal-code-2",
                industrialZone = "industrial-zone-2",
                building = "building-2",
                floor = "floor-2",
                door = "door-2"
            ),
            alternativePostalAddress = AlternativePostalAddressDto(
                geographicCoordinates = GeoCoordinateDto(0.2f, 0.2f, 0.2f),
                country = CountryCode.FR,
                administrativeAreaLevel1 = "FR-BFC",
                postalCode = "alt-post-code-2",
                city = "alt-city-2",
                deliveryServiceNumber = "delivery-service-number-2",
                deliveryServiceQualifier = "delivery-service-qualifier-2",
                deliveryServiceType = DeliveryServiceType.BOITE_POSTALE
            ),
            confidenceCriteria = ConfidenceCriteriaDto(
                sharedByOwner = true,
                checkedByExternalDataSource = true,
                numberOfBusinessPartners = 7,
                lastConfidenceCheckAt = LocalDateTime.of(2022, 4, 3, 2, 1),
                nextConfidenceCheckAt = LocalDateTime.of(2026, 4, 3, 2, 1),
                confidenceLevel = 1
            )
        ),
        ownerBpnL = "BPNL_OWNER_TEST_2"
    )


    val logisticAddress1 = LogisticAddressDto(
        name = "Address Name 1",
        states = listOf(
            AddressStateDto(
                validFrom = LocalDateTime.of(1970, 4, 4, 4, 4),
                validTo = LocalDateTime.of(1975, 5, 5, 5, 5),
                type = BusinessStateType.ACTIVE
            ),
            AddressStateDto(
                validFrom = LocalDateTime.of(1975, 5, 5, 5, 5),
                validTo = null,
                type = BusinessStateType.INACTIVE
            ),
        ),
        identifiers = listOf(
            AddressIdentifierDto(
                value = "Address Identifier Value 1",
                type = "Address Identifier Type 1"
            ),
            AddressIdentifierDto(
                value = "Address Identifier Value 2",
                type = "Address Identifier Type 2"
            )
        ),
        physicalPostalAddress = PhysicalPostalAddressDto(
            geographicCoordinates = GeoCoordinateDto(0.12f, 0.12f, 0.12f),
            country = CountryCode.AD,
            administrativeAreaLevel1 = "AD-07",
            administrativeAreaLevel2 = "Admin-Level 2-1",
            administrativeAreaLevel3 = "Admin-Level 3-1",
            postalCode = "Postal Code 1",
            city = "City 1",
            district = "District 1",
            street = StreetDto(
                name = "Street Name 1",
                houseNumber = "House Number 1",
                houseNumberSupplement = "house-number-supplement-1",
                milestone = "Milestone 1",
                direction = "Direction 1",
                namePrefix = "Name Prefix 1",
                additionalNameSuffix = "Additional Name Suffix 1",
                additionalNamePrefix = "Additional Name Prefix 1",
                nameSuffix = "Name Suffix 1"
            ),
            companyPostalCode = "Company Postal Code 1",
            industrialZone = "Industrial Zone 1",
            building = "Building 1",
            floor = "Floor 1",
            door = "Door 1"
        ),
        alternativePostalAddress = AlternativePostalAddressDto(
            geographicCoordinates = GeoCoordinateDto(0.23f, 0.23f, 0.23f),
            country = CountryCode.AD,
            administrativeAreaLevel1 = "AD-08",
            postalCode = "Postal Code Alt 1",
            city = "City Alt 1",
            deliveryServiceType = DeliveryServiceType.PRIVATE_BAG,
            deliveryServiceQualifier = "Delivery Service Qualifier 1",
            deliveryServiceNumber = "Delivery Service Number 1"
        ),
        bpnAReference = BpnReferenceDto(
            referenceType = BpnReferenceType.Bpn,
            referenceValue = "BPNATEST-1"
        ),
        hasChanged = true,
        confidenceCriteria = ConfidenceCriteriaDto(
            sharedByOwner = true,
            checkedByExternalDataSource = true,
            numberOfBusinessPartners = 7,
            lastConfidenceCheckAt = LocalDateTime.of(2022, 4, 3, 2, 1),
            nextConfidenceCheckAt = LocalDateTime.of(2026, 4, 3, 2, 1),
            confidenceLevel = 1
        )
    )

    val logisticAddress2 = LogisticAddressDto(
        name = "Address Name 2",
        states = listOf(
            AddressStateDto(
                validFrom = LocalDateTime.of(1971, 4, 4, 4, 4),
                validTo = null,
                type = BusinessStateType.ACTIVE
            )
        ),
        identifiers = listOf(
            AddressIdentifierDto(
                value = "Address Identifier Value 2-1",
                type = "Address Identifier Type 2-1"
            )
        ),
        physicalPostalAddress = PhysicalPostalAddressDto(
            geographicCoordinates = GeoCoordinateDto(0.45f, 0.46f, 0.47f),
            country = CountryCode.AD,
            administrativeAreaLevel1 = "AD-07",
            administrativeAreaLevel2 = "Admin-Level 2-2",
            administrativeAreaLevel3 = "Admin-Level 3-2",
            postalCode = "Postal Code 2",
            city = "City 2",
            district = "District 2",
            street = StreetDto(
                name = "Street Name 2",
                houseNumber = "House Number 2",
                houseNumberSupplement = "house-number-supplement-2",
                milestone = "Milestone 2",
                direction = "Direction 2",
                namePrefix = "Name Prefix 2",
                additionalNameSuffix = "Additional Name Suffix 2",
                additionalNamePrefix = "Additional Name Prefix 2",
                nameSuffix = "Name Suffix 2"
            ),
            companyPostalCode = "Company Postal Code 2",
            industrialZone = "Industrial Zone 2",
            building = "Building 2",
            floor = "Floor 2",
            door = "Door 2"
        ),
        alternativePostalAddress = AlternativePostalAddressDto(
            geographicCoordinates = GeoCoordinateDto(0.01f, 0.02f, 0.03f),
            country = CountryCode.AD,
            administrativeAreaLevel1 = "AD-08",
            postalCode = "Postal Code Alt 2",
            city = "City Alt 2",
            deliveryServiceType = DeliveryServiceType.PO_BOX,
            deliveryServiceQualifier = "Delivery Service Qualifier 2",
            deliveryServiceNumber = "Delivery Service Number 2"
        ),
        bpnAReference = BpnReferenceDto(
            referenceType = BpnReferenceType.BpnRequestIdentifier,
            referenceValue = "BPN_REQUEST_ID_TEST"
        ),
        hasChanged = true,
        confidenceCriteria = ConfidenceCriteriaDto(
            sharedByOwner = false,
            checkedByExternalDataSource = false,
            numberOfBusinessPartners = 2,
            lastConfidenceCheckAt = LocalDateTime.of(2023, 5, 6, 7, 8),
            nextConfidenceCheckAt = LocalDateTime.of(2026, 5, 6, 7, 8),
            confidenceLevel = 2
        )
    )

    val legalEntity1 = LegalEntityDto(
        legalName = "Legal Entity Name 1",
        legalShortName = "Legal Short Name 1",
        identifiers = listOf(
            LegalEntityIdentifierDto(
                value = "Legal Identifier Value 1",
                type = "Legal Identifier Type 1",
                issuingBody = "Legal Issuing Body 1"
            ),
            LegalEntityIdentifierDto(
                value = "Legal Identifier Value 2",
                type = "Legal Identifier Type 2",
                issuingBody = "Legal Issuing Body 2"
            ),
        ),
        legalForm = "Legal Form 1",
        states = listOf(
            LegalEntityStateDto(
                validFrom = LocalDateTime.of(1995, 2, 2, 3, 3),
                validTo = LocalDateTime.of(2000, 3, 3, 4, 4),
                type = BusinessStateType.ACTIVE
            ),
            LegalEntityStateDto(
                validFrom = LocalDateTime.of(2000, 3, 3, 4, 4),
                validTo = null,
                type = BusinessStateType.INACTIVE
            ),
        ),
        classifications = listOf(
            LegalEntityClassificationDto(
                type = ClassificationType.SIC,
                code = "Classification Code 1",
                value = "Classification Value 1"
            ),
            LegalEntityClassificationDto(
                type = ClassificationType.NACE,
                code = "Classification Code 2",
                value = "Classification Value 2"
            )
        ),
        legalAddress = logisticAddress1,
        bpnLReference = BpnReferenceDto(
            referenceValue = "BPNL1-TEST",
            referenceType = BpnReferenceType.Bpn
        ),
        hasChanged = false,
        confidenceCriteria = ConfidenceCriteriaDto(
            sharedByOwner = false,
            checkedByExternalDataSource = false,
            numberOfBusinessPartners = 2,
            lastConfidenceCheckAt = LocalDateTime.of(2023, 5, 6, 7, 8),
            nextConfidenceCheckAt = LocalDateTime.of(2026, 5, 6, 7, 8),
            confidenceLevel = 3
        )
    )

    val legalEntity2 = LegalEntityDto(
        legalName = "Legal Entity Name 2",
        legalShortName = "Legal Short Name 2",
        identifiers = listOf(
            LegalEntityIdentifierDto(
                value = "Legal Identifier Value 2",
                type = "Legal Identifier Type 2",
                issuingBody = "Legal Issuing Body 2"
            )
        ),
        legalForm = "Legal Form 2",
        states = listOf(
            LegalEntityStateDto(
                validFrom = LocalDateTime.of(1900, 5, 5, 5, 5),
                validTo = null,
                type = BusinessStateType.ACTIVE
            )
        ),
        classifications = listOf(
            LegalEntityClassificationDto(
                type = ClassificationType.SIC,
                code = "Classification Code 2",
                value = "Classification Value 2"
            )
        ),
        legalAddress = logisticAddress2,
        bpnLReference = BpnReferenceDto(
            referenceValue = "BPNL-REQUEST_ID_TEST",
            referenceType = BpnReferenceType.BpnRequestIdentifier
        ),
        hasChanged = false,
        confidenceCriteria = ConfidenceCriteriaDto(
            sharedByOwner = false,
            checkedByExternalDataSource = false,
            numberOfBusinessPartners = 2,
            lastConfidenceCheckAt = LocalDateTime.of(2023, 5, 6, 7, 8),
            nextConfidenceCheckAt = LocalDateTime.of(2026, 5, 6, 7, 8),
            confidenceLevel = 4
        )
    )

    val site1 = SiteDto(
        name = "Site Name 1",
        states = listOf(
            SiteStateDto(
                validFrom = LocalDateTime.of(1991, 10, 10, 10, 10),
                validTo = LocalDateTime.of(2001, 11, 11, 11, 11),
                type = BusinessStateType.ACTIVE
            ),
            SiteStateDto(
                validFrom = LocalDateTime.of(2001, 11, 11, 11, 11),
                validTo = null,
                type = BusinessStateType.INACTIVE
            )
        ),
        mainAddress = logisticAddress1,
        bpnSReference = BpnReferenceDto(
            referenceValue = "BPNS_TEST",
            referenceType = BpnReferenceType.Bpn
        ),
        hasChanged = false,
        confidenceCriteria = ConfidenceCriteriaDto(
            sharedByOwner = false,
            checkedByExternalDataSource = false,
            numberOfBusinessPartners = 2,
            lastConfidenceCheckAt = LocalDateTime.of(2023, 5, 6, 7, 8),
            nextConfidenceCheckAt = LocalDateTime.of(2026, 5, 6, 7, 8),
            confidenceLevel = 5
        )
    )

    val site2 = SiteDto(
        name = "Site Name 2",
        states = listOf(
            SiteStateDto(
                validFrom = LocalDateTime.of(1997, 12, 12, 12, 12),
                validTo = null,
                type = BusinessStateType.ACTIVE
            )
        ),
        mainAddress = logisticAddress2,
        bpnSReference = BpnReferenceDto(
            referenceValue = "BPNS_REQUEST_ID_TEST",
            referenceType = BpnReferenceType.BpnRequestIdentifier
        ),
        hasChanged = true,
        confidenceCriteria = ConfidenceCriteriaDto(
            sharedByOwner = false,
            checkedByExternalDataSource = false,
            numberOfBusinessPartners = 2,
            lastConfidenceCheckAt = LocalDateTime.of(2023, 5, 6, 7, 8),
            nextConfidenceCheckAt = LocalDateTime.of(2026, 5, 6, 7, 8),
            confidenceLevel = 6
        )
    )

    val businessPartner1Full = BusinessPartnerFullDto(
        generic = businessPartner1,
        legalEntity = legalEntity1,
        site = site1,
        address = logisticAddress1
    )

    val businessPartner2Full = BusinessPartnerFullDto(
        generic = businessPartner2,
        legalEntity = legalEntity2,
        site = site2,
        address = logisticAddress2
    )

}