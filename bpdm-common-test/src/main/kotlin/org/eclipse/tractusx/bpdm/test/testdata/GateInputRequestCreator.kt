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

package org.eclipse.tractusx.bpdm.test.testdata


import com.neovisionaries.i18n.CountryCode
import org.eclipse.tractusx.bpdm.common.dto.BusinessPartnerRole
import org.eclipse.tractusx.bpdm.common.dto.GeoCoordinateDto
import org.eclipse.tractusx.bpdm.common.dto.TypeKeyNameVerboseDto
import org.eclipse.tractusx.bpdm.common.model.BusinessStateType
import org.eclipse.tractusx.bpdm.common.model.ClassificationType
import org.eclipse.tractusx.bpdm.common.model.DeliveryServiceType
import org.eclipse.tractusx.bpdm.gate.api.model.*
import org.eclipse.tractusx.bpdm.gate.api.model.request.AddressGateInputRequest
import org.eclipse.tractusx.bpdm.gate.api.model.request.LegalEntityGateInputRequest
import org.eclipse.tractusx.bpdm.gate.api.model.request.SiteGateInputRequest
import org.eclipse.tractusx.bpdm.test.testdata.pool.BusinessPartnerVerboseValues
import java.time.LocalDateTime


fun fullValidGateLegalEntity(externalId: String): LegalEntityGateInputRequest {

    return LegalEntityGateInputRequest(fullGateLegalEntity(externalId), fullGateLogisticAddress(externalId), externalId =
    "legal_entity_$externalId")
}

fun fullValidGateSite(externalId: String): SiteGateInputRequest {

    return SiteGateInputRequest(fullGateSite(externalId), fullGateLogisticAddress(externalId),
        externalId = "site_$externalId",
        legalEntityExternalId = "legal_entity_$externalId"
        )
}

fun fullValidGateAddress(externalId: String): AddressGateInputRequest {

    return AddressGateInputRequest(fullGateLogisticAddress("$externalId"),
        externalId = "address_$externalId",
        legalEntityExternalId = "legal_entity_$externalId",
        siteExternalId = "site_$externalId")
}

fun fullGateSite(externalId: String): SiteGateDto {

    return SiteGateDto(
        nameParts = listOf("legal_name_parts"),
        states = listOf(
            fullGateSiteState(
                externalId, 1L,
                BusinessStateType.ACTIVE
            ),
            fullGateSiteState(
                externalId, 2L,
                BusinessStateType.INACTIVE
            )
        ),
        roles = listOf(BusinessPartnerRole.SUPPLIER,BusinessPartnerRole.CUSTOMER)
    )
}

fun fullGateLegalEntity(externalId: String): LegalEntityDto {

    return LegalEntityDto(
        legalShortName = "legal_short_name_$externalId",
        identifiers = listOf(
            fullGateLegalIdentifierDto(externalId, 1L, BusinessPartnerVerboseValues.identifierType1),
            fullGateLegalIdentifierDto(externalId, 2L, BusinessPartnerVerboseValues.identifierType2)
        ),
        legalNameParts = listOf("legal_name_parts"),
        legalForm = BusinessPartnerVerboseValues.legalForm1.technicalKey,
        states = listOf(
            fullGateLegalEntityState(
                externalId, 1L,
                BusinessStateType.ACTIVE
            ),
            fullGateLegalEntityState(
                externalId, 2L,
                BusinessStateType.INACTIVE
            )
        ),
        classifications = listOf(
            fullGateLegalEntityClassification(externalId, 1L, ClassificationType.SIC),
            fullGateLegalEntityClassification(externalId, 2L, ClassificationType.NACE)
        )
    )
}

fun fullGateLogisticAddress(externalId: String): LogisticAddressDto {

    return LogisticAddressDto(
        nameParts = listOf("name_parts"),
        identifiers = listOf(

        ),
        states = listOf(
            fullGateAddressState(externalId, 1L, BusinessStateType.ACTIVE),
            fullGateAddressState(externalId, 2L, BusinessStateType.INACTIVE)
        ),
        physicalPostalAddress = fullGatePhysicalPostalAddressDto(externalId),
        alternativePostalAddress = fullGateAlternativePostalAddressDto(externalId)
    )
}

fun fullGatePhysicalPostalAddressDto(externalId: String) = PhysicalPostalAddressDto(
    geographicCoordinates = GeoCoordinateDto(longitude = 1.1f, latitude = 2.2f, altitude = 3.3f),
    country = CountryCode.DE,
    administrativeAreaLevel1 = "AD-07",
    administrativeAreaLevel2 = "adminArea2_" + externalId,
    administrativeAreaLevel3 = "adminArea3_" + externalId,
    postalCode = "postalCode_" + externalId,
    city = "city_" + externalId,
    street = StreetDto(
        name = "name_" + externalId,
        houseNumber = "houseNumber_" + externalId,
        milestone = "milestone_" + externalId,
        direction = "direction_" + externalId,
        namePrefix = "namePrefix_" + externalId,
        additionalNamePrefix = "additionalNamePrefix_" + externalId,
        nameSuffix = "nameSuffix_" + externalId,
        additionalNameSuffix = "additionalNameSuffix_" + externalId,
    ),
    district = "district_" + externalId,
    companyPostalCode = "companyPostalCode_" + externalId,
    industrialZone = "industrialZone_" + externalId,
    building = "building_" + externalId,
    floor = "floor_" + externalId,
    door = "door_" + externalId,
)

fun fullGateAlternativePostalAddressDto(externalId: String) = AlternativePostalAddressDto(

    geographicCoordinates = GeoCoordinateDto(longitude = 12.3f, latitude = 4.56f, altitude = 7.89f),
    country = CountryCode.DE,
    administrativeAreaLevel1 = "DE-BW",
    postalCode = "alternate_postalCode_" + externalId,
    city = "alternate_city_" + externalId,
    deliveryServiceType = DeliveryServiceType.PO_BOX,
    deliveryServiceQualifier = "deliveryServiceQualifier_" + externalId,
    deliveryServiceNumber = "deliveryServiceNumber_" + externalId,

    )

fun fullGateAddressIdentifierDto(name: String, id: Long, type: TypeKeyNameVerboseDto<String>): AddressIdentifierDto {

    return AddressIdentifierDto(
        value = "value_" + name + "_" + id,
        type = type.technicalKey
    )
}

fun fullGateAddressState(name: String, id: Long, type: BusinessStateType): AddressStateDto {

    return AddressStateDto(
        validFrom = LocalDateTime.now().plusDays(id),
        validTo = LocalDateTime.now().plusDays(id + 2),
        type = type,
        description = "description_$name"
    )
}

fun fullGateLegalIdentifierDto(name: String, id: Long, type: TypeKeyNameVerboseDto<String>): LegalEntityIdentifierDto {

    return LegalEntityIdentifierDto(
        value = "value_" + name + "_" + id,
        type = type.technicalKey,
        issuingBody = "issuing_body_$name"
    )
}

fun fullGateLegalEntityState(name: String, id: Long, type: BusinessStateType): LegalEntityStateDto {

    return LegalEntityStateDto(
        description = "description_$name",
        validFrom = LocalDateTime.now().plusDays(id),
        validTo = LocalDateTime.now().plusDays(id + 2),
        type = type
    )
}

fun fullGateSiteState(name: String, id: Long, type: BusinessStateType): SiteStateDto {

    return SiteStateDto(
        description = "description_$name",
        validFrom = LocalDateTime.now().plusDays(id),
        validTo = LocalDateTime.now().plusDays(id + 2),
        type = type
    )
}

fun fullGateLegalEntityClassification(name: String, id: Long, type: ClassificationType): LegalEntityClassificationDto {

    return LegalEntityClassificationDto(
        code = "code_" + name + "_" + id,
        value = "value_" + name + "_" + id,
        type = type
    )
}


