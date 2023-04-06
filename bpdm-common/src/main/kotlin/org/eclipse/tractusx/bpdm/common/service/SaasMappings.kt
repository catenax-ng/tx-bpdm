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

package org.eclipse.tractusx.bpdm.common.service

import com.neovisionaries.i18n.CountryCode
import com.neovisionaries.i18n.LanguageCode
import mu.KotlinLogging
import org.eclipse.tractusx.bpdm.common.dto.*
import org.eclipse.tractusx.bpdm.common.dto.saas.*
import org.eclipse.tractusx.bpdm.common.exception.BpdmMappingException
import org.eclipse.tractusx.bpdm.common.exception.BpdmNullMappingException
import org.eclipse.tractusx.bpdm.common.model.DeliveryServiceType
import org.eclipse.tractusx.bpdm.common.model.HasDefaultValue

object SaasMappings {

    private val logger = KotlinLogging.logger { }

    const val BPN_TECHNICAL_KEY = "CX_BPN"

    fun findBpn(identifiers: Collection<IdentifierSaas>): String? {
        return identifiers.find { it.type?.technicalKey == BPN_TECHNICAL_KEY }?.value
    }

    private fun toReference(type: TypeKeyNameUrlSaas?): String {
        return type!!.technicalKey!!
    }

    private fun toOptionalReference(type: TypeKeyNameUrlSaas?): String? {
        return type?.technicalKey
    }

    private fun toOptionalReference(type: TypeKeyNameSaas?): String? {
        return type?.technicalKey
    }

    fun toOptionalReference(legalForm: LegalFormSaas?): String? {
        return legalForm?.technicalKey
    }

    private inline fun <reified T> toType(type: TypeKeyNameUrlSaas): T where T : Enum<T> {
        return enumValueOf(type.technicalKey!!)
    }

    inline fun <reified T> toTypeOrDefault(type: TypeKeyNameUrlSaas?): T where T : Enum<T>, T : HasDefaultValue<T> {
        return technicalKeyToType(type?.technicalKey)
    }

    inline fun <reified T> technicalKeyToType(technicalKey: String?): T where T : Enum<T>, T : HasDefaultValue<T> {
        val allValues = enumValues<T>()
        val foundValue = if (technicalKey != null) allValues.map { it.name }.find { technicalKey == it } else null
        return if (foundValue != null) enumValueOf(foundValue) else allValues.first().getDefault()
    }

    fun toLanguageCode(language: LanguageSaas?): LanguageCode {
        return language?.technicalKey ?: LanguageCode.undefined
    }

    inline fun <reified T> toTypeOrDefault(type: TypeKeyNameSaas?): T where T : Enum<T>, T : HasDefaultValue<T> {
        return technicalKeyToType(type?.technicalKey)
    }

    fun toCountryCode(country: CountrySaas?): CountryCode {
        return country?.shortName ?: CountryCode.UNDEFINED
    }

    fun BusinessPartnerSaas.toLegalEntityDto(): LegalEntityDto {
        val legalName = toNameDto()
            ?: throw BpdmMappingException(this::class, LegalEntityDto::class, "No legal name", externalId ?: "Unknown")
        return LegalEntityDto(
            identifiers = identifiers.filter { it.type?.technicalKey != BPN_TECHNICAL_KEY }.map { toLegalEntityIdentifierDto(it) },
            legalName = legalName,
            legalForm = toOptionalReference(legalForm),
            states = toLegalEntityStatesDtos(status),
            classifications = toDto(profile),
            // Known issue: For now the legal address is not a separate business partner in SaaS, therefore its properties name, states, identifiers are missing!
            legalAddress = convertSaasAdressesToDtoInternal(addresses, id)
        )
    }

    fun BusinessPartnerSaas.toSiteDto(): SiteDto {
        val name = toNameDto()
            ?: throw BpdmMappingException(this::class, SiteDto::class, "No name", externalId ?: "Unknown")
        return SiteDto(
            name = name.value,
            states = toSiteStatesDtos(status),
            // Known issue: For now the main address is not a separate business partner in SaaS, therefore its properties name, states, identifiers are missing!
            mainAddress = convertSaasAdressesToDtoInternal(addresses, id)
        )
    }

    fun BusinessPartnerSaas.toAddressDto(): LogisticAddressDto {
        // partial LogisticAddressDto is enriched with info from BusinessPartnerSaas
        return convertSaasAdressesToDtoInternal(addresses, id)
            .copy(
                name = toNameDto()?.value,
                states = toAddressStatesDtos(status),
                identifiers = identifiers.filter { it.type?.technicalKey != BPN_TECHNICAL_KEY }.map { toAddressIdentifierDto(it) }
            )
    }

    private fun BusinessPartnerSaas.toNameDto(): NameDto? {
        if (names.size > 1) {
            logger.warn { "Business Partner with ID $externalId has more than one name" }
        }
        return names.map { toDto(it) }
            .firstOrNull()
    }

    fun toLegalEntityIdentifierDto(identifier: IdentifierSaas): LegalEntityIdentifierDto {
        return LegalEntityIdentifierDto(
            value = identifier.value ?: throw BpdmNullMappingException(IdentifierSaas::class, LegalEntityIdentifierDto::class, IdentifierSaas::value),
            type = toReference(identifier.type),
            issuingBody = identifier.issuingBody?.name
        )
    }

    fun toAddressIdentifierDto(identifier: IdentifierSaas): AddressIdentifierDto {
        return AddressIdentifierDto(
            value = identifier.value ?: throw BpdmNullMappingException(IdentifierSaas::class, AddressIdentifierDto::class, IdentifierSaas::value),
            type = toReference(identifier.type)
        )
    }

    fun toDto(name: NameSaas): NameDto {
        return NameDto(
            name.value,
            name.shortName
        )
    }

    fun toLegalEntityStatesDtos(status: BusinessPartnerStatusSaas?): Collection<LegalEntityStateDto> =
        listOfNotNull(
            status?.type?.let {
                LegalEntityStateDto(
                    officialDenotation = status.officialDenotation,
                    validFrom = status.validFrom,
                    validTo = status.validUntil,
                    type = toType(status.type)
                )
            }
        )

    fun toSiteStatesDtos(status: BusinessPartnerStatusSaas?): Collection<SiteStateDto> =
        listOfNotNull(
            status?.type?.let {
                SiteStateDto(
                    description = status.officialDenotation,
                    validFrom = status.validFrom,
                    validTo = status.validUntil,
                    type = toType(status.type)
                )
            }
        )

    fun toAddressStatesDtos(status: BusinessPartnerStatusSaas?): Collection<AddressStateDto> =
        listOfNotNull(
            status?.type?.let {
                AddressStateDto(
                    description = status.officialDenotation,
                    validFrom = status.validFrom,
                    validTo = status.validUntil,
                    type = toType(status.type)
                )
            }
        )

    fun toDto(profile: PartnerProfileSaas?): Collection<ClassificationDto> {
        return profile?.classifications?.mapNotNull { toDto(it) } ?: emptyList()
    }

    fun toDto(classification: ClassificationSaas): ClassificationDto? {
        return classification.type?.let {
            ClassificationDto(
                value = classification.value,
                code = classification.code,
                type = toType(it)
            )
        }
    }

    /**
     * For now a legal/main address is not represented as a full-fledged business partner like a regular address, but as a sub-address of its
     * leading business partner (LE or site).
     *
     * This entails some limitations for legal/main addresses compared to regular addresses:
     * - name, states, identifiers are missing in SaaS
     * - a generated BPN-A can't be returned back to the Gate
     */
    private fun convertSaasAdressesToDtoInternal(addresses: Collection<AddressSaas>, id: String?): LogisticAddressDto {

        val mapping = SaasAddressesMapping(addresses)
        val physicalAddressMapping = mapping.saasPhysicalAddressMapping()
            ?: throw BpdmMappingException(AddressSaas::class, LogisticAddressDto::class, "No valid legal address", id ?: "Unknown")
        val alternativeAddressMapping = mapping.saasAlternativeAddressMapping()

        // info for name, states, identifiers is contained in BusinessPartnerSaas and can't be filled in here
        return LogisticAddressDto(
            name = null,
            states = emptyList(),
            identifiers = emptyList(),
            physicalPostalAddress = toPhysicalAddress(physicalAddressMapping, id),
            alternativePostalAddress = alternativeAddressMapping?.let { toAlternativeAddress(it, id) },
        )
    }

    fun toPhysicalAddress(map: SaasAddressToDtoMapping, id: String?): PhysicalPostalAddressDto {

        val city = map.city()
        val country = map.countryCode()
        if (city == null || country == null) {
            throw BpdmMappingException(AddressSaas::class, LogisticAddressDto::class, "No valid physical address", id ?: "Unknown")
        }

        return PhysicalPostalAddressDto(
            industrialZone = map.industrialZone(),
            building = map.building(),
            floor = map.floor(),
            door = map.door(),
            baseAddress = BasePostalAddressDto(
                geographicCoordinates = map.geoCoordinates(),
                city = city,
                country = country,
                administrativeAreaLevel1 = map.adminAreaLevel1(),
                administrativeAreaLevel2 = map.adminAreaLevel2(),
                administrativeAreaLevel3 = null,
                administrativeAreaLevel4 = null,
                postCode = map.postcode(),
                districtLevel1 = map.districtLevel1(),
                districtLevel2 = map.districtLevel2(),
                street = toStreetDto(map),
            )
        )
    }

    private fun toStreetDto(map: SaasAddressToDtoMapping): StreetDto? {
        var streetDto: StreetDto? = null
        if (map.streetName() != null) {
            streetDto = StreetDto(
                name = map.streetName(),
                houseNumber = map.streetHouseNumber(),
                milestone = map.streetMilestone(),
                direction = map.streetDirection()
            )
        }
        return streetDto
    }

    fun toAlternativeAddress(map: SaasAddressToDtoMapping, id: String?): AlternativePostalAddressDto {

        val city = map.city()
        val country = map.countryCode()
        if (city == null || country == null) {
            throw BpdmMappingException(AddressSaas::class, LogisticAddressDto::class, "No valid alternativ address", id ?: "Unknown")
        }

        val poBoxValue = map.deliveryServiceTypePoBox()
        val privateBagValue = map.deliveryServiceTypePrivateBag()

        var deliveryType: DeliveryServiceType? = null
        var deliveryValue: String? = null
        if (poBoxValue != null) {
            deliveryType = DeliveryServiceType.PO_BOX
            deliveryValue = poBoxValue
        }
        if (privateBagValue != null) {
            deliveryType = DeliveryServiceType.PRIVATE_BAG
            deliveryValue = privateBagValue
        }

        if (deliveryValue == null || deliveryType == null) {
            throw BpdmMappingException(AddressSaas::class, LogisticAddressDto::class, "No valid alternativ address", id ?: "Unknown")
        }

        return AlternativePostalAddressDto(
            deliveryServiceNumber = deliveryValue,
            type = deliveryType,
            baseAddress = BasePostalAddressDto(
                geographicCoordinates = map.geoCoordinates(),
                city = city,
                country = country,
                administrativeAreaLevel1 = map.adminAreaLevel1(),
                administrativeAreaLevel2 = map.adminAreaLevel2(),
                administrativeAreaLevel3 = null,
                administrativeAreaLevel4 = null,
                postCode = map.postcode(),
                districtLevel1 = map.districtLevel1(),
                districtLevel2 = map.districtLevel2(),
                street = toStreetDto(map),
            )
        )
    }


    fun toDto(version: AddressVersionSaas?): AddressVersionDto {
        return AddressVersionDto(toTypeOrDefault(version?.characterSet), toLanguageCode(version?.language))
    }


    fun toDto(geoCoords: GeoCoordinatesSaas): GeoCoordinateDto? {
        return if (geoCoords.latitude != null && geoCoords.longitude != null) GeoCoordinateDto(geoCoords.longitude, geoCoords.latitude, null) else null
    }

    fun toRelationToDelete(relation: RelationSaas): DeleteRelationsRequestSaas.RelationToDeleteSaas {
        return DeleteRelationsRequestSaas.RelationToDeleteSaas(
            startNode = DeleteRelationsRequestSaas.RelationNodeToDeleteSaas(
                dataSourceId = relation.startNodeDataSource,
                externalId = relation.startNode
            ),
            endNode = DeleteRelationsRequestSaas.RelationNodeToDeleteSaas(
                dataSourceId = relation.endNodeDataSource,
                externalId = relation.endNode
            )
        )
    }

}