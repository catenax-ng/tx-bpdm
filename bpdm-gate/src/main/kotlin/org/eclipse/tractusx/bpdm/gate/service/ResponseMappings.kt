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

import com.neovisionaries.i18n.CurrencyCode
import org.eclipse.tractusx.bpdm.common.dto.*
import org.eclipse.tractusx.bpdm.common.dto.response.PageResponse
import org.eclipse.tractusx.bpdm.gate.dto.AddressGateInputRequest
import org.eclipse.tractusx.bpdm.gate.dto.LegalEntityGateInputRequest
import org.eclipse.tractusx.bpdm.common.dto.AddressDto
import org.eclipse.tractusx.bpdm.common.dto.AddressVersionDto
import org.eclipse.tractusx.bpdm.common.dto.GeoCoordinateDto
import org.eclipse.tractusx.bpdm.common.dto.SiteDto
import org.eclipse.tractusx.bpdm.gate.dto.SiteGateInputRequest
import org.eclipse.tractusx.bpdm.gate.entity.*
import java.time.Instant
import java.time.temporal.ChronoUnit


fun AddressDto.toAddressGateDto(): AddressGate {

    return AddressGate(
        this.careOf,
        this.contexts.toMutableSet(),
        this.country,
        this.types.toMutableSet(),
        this.version.toAddressVersionGate(),
        this.geographicCoordinates?.toGeographicCoordinateGate(),
        "",
        "",
        "",
        ""
    )
}

fun AddressGateInputRequest.toAddressGate(): AddressGate {

    val geoCoords = this.address.geographicCoordinates?.let {
        GeoCoordinateDto(
            it.longitude,
            it.latitude,
            it.altitude
        ).toGeographicCoordinateGate()
    }

    val address = AddressGate(
        this.address.careOf,
        this.address.contexts.toMutableSet(),
        this.address.country,
        this.address.types.toMutableSet(),
        this.address.version.toAddressVersionGate(),
        geoCoords,
        this.externalId,
        this.legalEntityExternalId.toString(),
        this.siteExternalId.toString(),
        this.bpn.toString(),
    )
    address.postCodes.replace(this.address.postCodes.map { toEntity(it, address) }.toSet())
    address.administrativeAreas.replace(this.address.administrativeAreas.map { toEntity(it, address) }.toSet())
    address.thoroughfares.replace(this.address.thoroughfares.map { toEntity(it, address) }.toSet())
    address.localities.replace(this.address.localities.map { toEntity(it, address) }.toSet())
    address.premises.replace(this.address.premises.map { toEntity(it, address) }.toSet())
    address.postalDeliveryPoints.replace(this.address.postalDeliveryPoints.map { toEntity(it, address) }.toSet())
    address.contexts.replace(this.address.contexts)
    address.types.replace(this.address.types)
    return address
}
fun <T> MutableCollection<T>.replace (elements : Collection<T>) {
    clear()
    addAll(elements)
}
fun toEntity(dto: PostCodeDto, address: AddressGate): PostCodeGate {
    return PostCodeGate(dto.value, dto.type, address)
}

fun toEntity(dto: AdministrativeAreaDto, address: AddressGate): AdministrativeAreaGate {
    return AdministrativeAreaGate(dto.value, dto.shortName, dto.fipsCode, dto.type, address.version.language, address.country, address)
}

fun toEntity(dto: ThoroughfareDto, address: AddressGate): ThoroughfareGate {
    return ThoroughfareGate(dto.value, dto.name, dto.shortName, dto.number, dto.direction, dto.type, address)
}

fun toEntity(dto: LocalityDto, address: AddressGate): LocalityGate {
    return LocalityGate(dto.value, dto.shortName, dto.type, address)
}

fun toEntity(dto: PremiseDto, address: AddressGate): PremiseGate {
    return PremiseGate(dto.value, dto.shortName, dto.number, dto.type, address)
}

fun toEntity(dto: PostalDeliveryPointDto, address: AddressGate): PostalDeliveryPointGate {
    return PostalDeliveryPointGate(dto.value, dto.shortName, dto.number, dto.type, address)
}

fun toEntity(dto: IdentifierDto,legalEntityGate: LegalEntityGate): IdentifierGate {
    return IdentifierGate(dto.value, IdentifierTypeGate(dto.type,"","") ,
        IdentifierStatusGate(dto.status.toString(),"") ,
        IssuingBodyGate(dto.issuingBody.toString(),"","") ,  legalEntityGate)
}
fun toEntity(dto: NameDto,legalEntityGate: LegalEntityGate): NameGate {
    return NameGate(dto.value,dto.shortName,dto.type,dto.language,legalEntityGate)
}
fun toEntity(dto: BankAccountDto,legalEntityGate: LegalEntityGate): BankAccountGate {
    return BankAccountGate(dto.trustScores.toMutableSet(), currency = dto.currency,
     dto.nationalBankAccountIdentifier.toString(),dto.internationalBankIdentifier.toString(),
        dto.nationalBankAccountIdentifier.toString(),dto.nationalBankIdentifier.toString(),legalEntityGate)
}
fun toEntity(dto: ClassificationDto,legalEntityGate: LegalEntityGate): ClassificationGate {
    return ClassificationGate(dto.value,dto.code,dto.type,legalEntityGate)
}
fun GeoCoordinateDto.toGeographicCoordinateGate(): GeographicCoordinateGate {

    return GeographicCoordinateGate(
        this.longitude,
        this.latitude,
        this.altitude
    )

}

fun AddressVersionDto.toAddressVersionGate(): AddressVersionGate {

    return AddressVersionGate(
        this.characterSet,
        this.language
    )

}

// Site Mappers
fun SiteGate.toSiteGateInputRequest(): SiteGateInputRequest {

    return SiteGateInputRequest(
        SiteDto(this.name, AddressDto()),
        this.bpn,
        this.legalEntityExternalId,
        this.externalId
    )
}

fun SiteGateInputRequest.toSiteGate(): SiteGate {

    return SiteGate(
        this.bpn.toString(),
        this.site.name,
        this.externalId,
        this.legalEntityExternalId,
        this.site.mainAddress.toAddressGateDto(),
    )
}

fun LegalEntityGateInputRequest.toLegalEntityGate(): LegalEntityGate{

    val legalEntityGate = LegalEntityGate(
        bpn= bpn.toString(),
        legalEntity.legalForm.toString(),
        currentness = createCurrentnessTimestamp(),
        externalId = externalId.toString(),
        types = legalEntity.types.toMutableSet(),
        legalAddress = legalEntity.legalAddress.toAddressGateDto(),
        roles = mutableSetOf()
    )

    legalEntityGate.identifiers.replace(this.legalEntity.identifiers.map { toEntity(it, legalEntityGate) }.toSet())
    legalEntityGate.nameGates.replace(this.legalEntity.names.map {toEntity(it, legalEntityGate)}.toSet())
    legalEntityGate.bankAccounts.replace(this.legalEntity.bankAccounts.map { toEntity(it,legalEntityGate) }.toSet())
    legalEntityGate.classification.replace(this.legalEntity.profileClassifications.map { toEntity(it, legalEntityGate) }.toSet())
    return legalEntityGate;
}
private fun createCurrentnessTimestamp(): Instant {
    return Instant.now().truncatedTo(ChronoUnit.MICROS)
}




