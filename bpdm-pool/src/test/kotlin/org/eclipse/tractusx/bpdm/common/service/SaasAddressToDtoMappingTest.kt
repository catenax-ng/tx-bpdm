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

import org.assertj.core.api.Assertions
import org.eclipse.tractusx.bpdm.common.dto.BasePostalAddressDto
import org.eclipse.tractusx.bpdm.common.dto.PhysicalPostalAddressDto
import org.eclipse.tractusx.bpdm.common.dto.response.PhysicalPostalAddressResponse
import org.eclipse.tractusx.bpdm.common.dto.saas.AddressSaas
import org.eclipse.tractusx.bpdm.common.dto.saas.TypeValueSaas
import org.eclipse.tractusx.bpdm.common.model.*
import org.eclipse.tractusx.bpdm.pool.util.ResponseValues
import org.eclipse.tractusx.bpdm.pool.util.SaasValues
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

class SaasAddressToDtoMappingTest {

    @Test
    fun addressPartnerTest() {

        checkMappingResponsePhysicalAddress( ResponseValues.addressPartner1.physicalPostalAddress, SaasValues.addressPartnerSaas1.addresses.first())
        checkMappingResponsePhysicalAddress( ResponseValues.addressPartner2.physicalPostalAddress, SaasValues.addressPartnerSaas2.addresses.first())
        checkMappingResponsePhysicalAddress( ResponseValues.addressPartner3.physicalPostalAddress, SaasValues.addressPartnerSaas3.addresses.first())
    }

    @Test
    fun saasPhysicalAddressMappingTest() {

        val addressesMapping = SaasAddressesMapping(SaasValues.addressPartner1.addresses)
        val address = addressesMapping.saasPhysicalAddressMapping()!!
        val physicalAddressDto = SaasMappings.toPhysicalAddress(address,  "")

        checkMappingDtoPhysicalAddress( physicalAddressDto, SaasValues.addressPartner1.addresses.first())

    }

    private fun checkMappingResponsePhysicalAddress(physicalAddressDto: PhysicalPostalAddressResponse, addressSass: AddressSaas) {

        val baseAddressDto = physicalAddressDto.baseAddress
//TODO        Assertions.assertThat(baseAddressDto.administrativeAreaLevel1?.name).isEqualTo(findValue(addressSass.administrativeAreas, SaasAdministrativeAreaType.REGION))
        Assertions.assertThat(baseAddressDto.administrativeAreaLevel2).isEqualTo(findValue(addressSass.administrativeAreas, SaasAdministrativeAreaType.COUNTY))
        Assertions.assertThat(baseAddressDto.administrativeAreaLevel3).isEqualTo(null)
        Assertions.assertThat(baseAddressDto.administrativeAreaLevel4).isEqualTo(null)
        Assertions.assertThat(baseAddressDto.city).isEqualTo(findValue(addressSass.localities, SaasLocalityType.CITY))
        Assertions.assertThat(baseAddressDto.country.technicalKey).isEqualTo(addressSass.country?.shortName)
        Assertions.assertThat(baseAddressDto.districtLevel1).isEqualTo(findValue(addressSass.localities, SaasLocalityType.DISTRICT))
        Assertions.assertThat(baseAddressDto.districtLevel2).isEqualTo(findValue(addressSass.localities, SaasLocalityType.QUARTER))
        Assertions.assertThat(baseAddressDto.geographicCoordinates?.latitude).isEqualTo(addressSass.geographicCoordinates?.latitude)
        Assertions.assertThat(baseAddressDto.geographicCoordinates?.longitude).isEqualTo(addressSass.geographicCoordinates?.longitude)
        Assertions.assertThat(baseAddressDto.postCode).isEqualTo(findValue(addressSass.postCodes, SaasPostCodeType.REGULAR))
        Assertions.assertThat(baseAddressDto.street?.name).isEqualTo(findObject(addressSass.thoroughfares, SaasThoroughfareType.STREET)?.name)
        Assertions.assertThat(baseAddressDto.street?.houseNumber).isEqualTo(findObject(addressSass.thoroughfares, SaasThoroughfareType.STREET)?.number)
        Assertions.assertThat(baseAddressDto.street?.direction).isEqualTo(findObject(addressSass.thoroughfares, SaasThoroughfareType.STREET)?.direction)
        Assertions.assertThat(baseAddressDto.street?.milestone).isEqualTo(findObject(addressSass.thoroughfares, SaasThoroughfareType.STREET)?.shortName)

        Assertions.assertThat(physicalAddressDto.industrialZone).isEqualTo(findObject(addressSass.thoroughfares, SaasThoroughfareType.INDUSTRIAL_ZONE)?.name)
        Assertions.assertThat(physicalAddressDto.building).isEqualTo(findValue(addressSass.premises, SaasPremiseType.BUILDING))
        Assertions.assertThat(physicalAddressDto.floor).isEqualTo(findValue(addressSass.premises, SaasPremiseType.LEVEL))
        Assertions.assertThat(physicalAddressDto.door).isEqualTo(findValue(addressSass.premises, SaasPremiseType.ROOM))
    }
    private fun checkMappingDtoPhysicalAddress(physicalAddressDto: PhysicalPostalAddressDto, addressSass: AddressSaas) {

        val baseAddressDto = physicalAddressDto.baseAddress
        Assertions.assertThat(baseAddressDto.administrativeAreaLevel1).isEqualTo(findValue(addressSass.administrativeAreas, SaasAdministrativeAreaType.REGION))
        Assertions.assertThat(baseAddressDto.administrativeAreaLevel2).isEqualTo(findValue(addressSass.administrativeAreas, SaasAdministrativeAreaType.COUNTY))
        Assertions.assertThat(baseAddressDto.administrativeAreaLevel3).isEqualTo(null)
        Assertions.assertThat(baseAddressDto.administrativeAreaLevel4).isEqualTo(null)
        Assertions.assertThat(baseAddressDto.city).isEqualTo(findValue(addressSass.localities, SaasLocalityType.CITY))
        Assertions.assertThat(baseAddressDto.country).isEqualTo(addressSass.country?.shortName)
        Assertions.assertThat(baseAddressDto.districtLevel1).isEqualTo(findValue(addressSass.localities, SaasLocalityType.DISTRICT))
        Assertions.assertThat(baseAddressDto.districtLevel2).isEqualTo(findValue(addressSass.localities, SaasLocalityType.QUARTER))
        Assertions.assertThat(baseAddressDto.geographicCoordinates?.latitude).isEqualTo(addressSass.geographicCoordinates?.latitude)
        Assertions.assertThat(baseAddressDto.geographicCoordinates?.longitude).isEqualTo(addressSass.geographicCoordinates?.longitude)
        Assertions.assertThat(baseAddressDto.postCode).isEqualTo(findValue(addressSass.postCodes, SaasPostCodeType.REGULAR))
        Assertions.assertThat(baseAddressDto.street?.name).isEqualTo(findObject(addressSass.thoroughfares, SaasThoroughfareType.STREET)?.name)
        Assertions.assertThat(baseAddressDto.street?.houseNumber).isEqualTo(findObject(addressSass.thoroughfares, SaasThoroughfareType.STREET)?.number)
        Assertions.assertThat(baseAddressDto.street?.direction).isEqualTo(findObject(addressSass.thoroughfares, SaasThoroughfareType.STREET)?.direction)
        Assertions.assertThat(baseAddressDto.street?.milestone).isEqualTo(findObject(addressSass.thoroughfares, SaasThoroughfareType.STREET)?.shortName)

        Assertions.assertThat(physicalAddressDto.industrialZone).isEqualTo(findObject(addressSass.thoroughfares, SaasThoroughfareType.INDUSTRIAL_ZONE)?.name)
        Assertions.assertThat(physicalAddressDto.building).isEqualTo(findValue(addressSass.premises, SaasPremiseType.BUILDING))
        Assertions.assertThat(physicalAddressDto.floor).isEqualTo(findValue(addressSass.premises, SaasPremiseType.LEVEL))
        Assertions.assertThat(physicalAddressDto.door).isEqualTo(findValue(addressSass.premises, SaasPremiseType.ROOM))
    }

    private fun <T : TypeValueSaas> findValue(values: Collection<T>, enumType: NamedType): String? {
        val valueObject = values.find { it.type?.technicalKey.equals(enumType.getTypeName()) }
        return valueObject?.value
    }
    private fun <T : TypeValueSaas> findObject(values: Collection<T>, enumType: NamedType): T? {
        val valueObject = values.find { it.type?.technicalKey.equals(enumType.getTypeName()) }
        return valueObject
    }
}