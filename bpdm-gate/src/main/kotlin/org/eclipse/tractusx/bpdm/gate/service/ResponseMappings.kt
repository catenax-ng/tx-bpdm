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

package org.eclipse.tractusx.bpdm.gate.service

import org.eclipse.tractusx.bpdm.common.dto.*
import org.eclipse.tractusx.bpdm.gate.api.model.AddressGateInputRequest
import org.eclipse.tractusx.bpdm.gate.api.model.response.ChangelogResponse
import org.eclipse.tractusx.bpdm.gate.entity.*

fun AddressGateInputRequest.toAddressGate(): LogisticAddress {

    val logisticAddress = LogisticAddress(
        bpn = bpn.toString(),
        externalId = externalId,
        legalEntityExternalId = legalEntityExternalId.toString(),
        siteExternalId = siteExternalId.toString(),
        name = address.name,
        physicalPostalAddress = address.physicalPostalAddress.toPhysicalPostalAddressEntity(),
        alternativePostalAddress = address.alternativePostalAddress?.toAlternativePostalAddressEntity()
    )

    //logisticAddress.identifiers.addAll(this.address.identifiers.map { toEntityIdentifier(it, logisticAddress) }.toSet())
    logisticAddress.states.addAll(this.address.states.map { toEntityAddress(it, logisticAddress) }.toSet())

    return logisticAddress
}

fun toEntityAddress(dto: AddressStateDto, address: LogisticAddress): AddressState {
    return AddressState(dto.description, dto.validFrom, dto.validTo, dto.type, address)
}

//fun toEntityIdentifier(dto: AddressIdentifierDto, address: LogisticAddress): AddressIdentifier {
//    return AddressIdentifier(dto.value, toIdentifierType(dto.type), address)
//}
//
//fun toIdentifierType(technicalKey: String): IdentifierType {
//    return IdentifierType(technicalKey, lsaType = null,"")
//}

fun AlternativePostalAddressDto.toAlternativePostalAddressEntity(): AlternativePostalAddress {

    return AlternativePostalAddress(
        geographicCoordinates = baseAddress.geographicCoordinates?.toGeographicCoordinateEntity(),
        country = baseAddress.country,
        administrativeAreaLevel1 = null, // TODO Add region mapping Logic
        administrativeAreaLevel2 = baseAddress.administrativeAreaLevel2,
        administrativeAreaLevel3 = baseAddress.administrativeAreaLevel3,
        administrativeAreaLevel4 = baseAddress.administrativeAreaLevel4,
        postCode = baseAddress.postCode,
        city = baseAddress.city,
        districtLevel1 = baseAddress.districtLevel1,
        districtLevel2 = baseAddress.districtLevel2,
        street = baseAddress.street?.toStreetEntity(),
        deliveryServiceType = type,
        deliveryServiceNumber = deliveryServiceNumber
    )

}

fun PhysicalPostalAddressDto.toPhysicalPostalAddressEntity(): PhysicalPostalAddress {

    return PhysicalPostalAddress(
        geographicCoordinates = baseAddress.geographicCoordinates?.toGeographicCoordinateEntity(),
        country = baseAddress.country,
        administrativeAreaLevel1 = null, // TODO Add region mapping Logic
        administrativeAreaLevel2 = baseAddress.administrativeAreaLevel2,
        administrativeAreaLevel3 = baseAddress.administrativeAreaLevel3,
        administrativeAreaLevel4 = baseAddress.administrativeAreaLevel4,
        postCode = baseAddress.postCode,
        city = baseAddress.city,
        districtLevel1 = baseAddress.districtLevel1,
        districtLevel2 = baseAddress.districtLevel2,
        street = baseAddress.street?.toStreetEntity(),
        industrialZone = industrialZone,
        building = building,
        floor = floor,
        door = door
    )

}

fun GeoCoordinateDto.toGeographicCoordinateEntity(): GeographicCoordinate {
    return GeographicCoordinate(longitude, latitude, altitude)
}

private fun StreetDto.toStreetEntity(): Street {
    return Street(
        name = name,
        houseNumber = houseNumber,
        milestone = milestone,
        direction = direction
    )
}

fun ChangelogEntry.toGateDto(): ChangelogResponse {
    return ChangelogResponse(
        externalId,
        businessPartnerType,
        createdAt
    )
}
