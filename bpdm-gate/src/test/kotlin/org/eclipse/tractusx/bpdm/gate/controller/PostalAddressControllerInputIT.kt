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

package org.eclipse.tractusx.bpdm.gate.controller

import com.github.tomakehurst.wiremock.client.WireMock.*
import com.github.tomakehurst.wiremock.core.WireMockConfiguration
import com.github.tomakehurst.wiremock.junit5.WireMockExtension
import org.assertj.core.api.Assertions.assertThat
import org.eclipse.tractusx.bpdm.common.dto.request.PaginationRequest
import org.eclipse.tractusx.bpdm.common.dto.response.PageDto
import org.eclipse.tractusx.bpdm.gate.api.client.GateClient
import org.eclipse.tractusx.bpdm.gate.repository.GateAddressRepository
import org.eclipse.tractusx.bpdm.gate.util.*
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.RegisterExtension
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpStatus
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration
import org.springframework.web.reactive.function.client.WebClientResponseException

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@ContextConfiguration(initializers = [PostgreSQLContextInitializer::class])
internal class PostalAddressControllerInputIT @Autowired constructor(
    val gateClient: GateClient,
    private val gateAddressRepository: GateAddressRepository,
    val testHelpers: DbTestHelpers,
) {
    companion object {
        @RegisterExtension
        private val wireMockServer: WireMockExtension = WireMockExtension.newInstance()
            .options(WireMockConfiguration.wireMockConfig().dynamicPort())
            .build()

    }

    @BeforeEach
    fun beforeEach() {
        wireMockServer.resetAll()
        testHelpers.truncateDbTables()
    }

    /**
     * Given address exists in the persistence database
     * When getting address by external id
     * Then address mapped to the catena data model should be returned
     */
    @Test
    fun `get address by external id`() {

        val externalIdToQuery = BusinessPartnerVerboseValues.externalIdAddress1
        val expectedAddress = BusinessPartnerVerboseValues.logisticAddressGateInputResponse1

        val legalEntity = listOf(
            BusinessPartnerNonVerboseValues.legalEntityGateInputRequest1
        )

        val site = listOf(
            BusinessPartnerNonVerboseValues.siteGateInputRequest1
        )

        val addresses = listOf(
            BusinessPartnerNonVerboseValues.addressGateInputRequest1
        )

        gateClient.legalEntities.upsertLegalEntities(legalEntity)
        gateClient.sites.upsertSites(site)
        gateClient.addresses.upsertAddresses(addresses)

        val valueResponse = gateClient.addresses.getAddressByExternalId(externalIdToQuery)

        assertThat(valueResponse).usingRecursiveComparison().ignoringFieldsMatchingRegexes(
            ".*legalEntityExternalId*",
            ".*siteExternalId*"
        ).isEqualTo(expectedAddress)
    }

    /**
     * Given address does not exist in the persistence database
     * When getting address by external id
     * Then "not found" response is sent
     */
    @Test
    fun `get address by external id, not found`() {

        try {
            gateClient.addresses.getAddressByExternalId("NONEXISTENT_BPN")
        } catch (e: WebClientResponseException) {
            assertEquals(HttpStatus.NOT_FOUND, e.statusCode)
        }
    }

    /**
     * Given addresses exists in the persistence database
     * When getting addresses page
     * Then addresses page mapped to the catena data model should be returned
     */

    @Test
    fun `get addresses`() {

        val expectedAddresses = listOf(
            BusinessPartnerVerboseValues.logisticAddressGateInputResponse1,
            BusinessPartnerVerboseValues.logisticAddressGateInputResponse2,
            BusinessPartnerVerboseValues.addressGateInputResponseLegalEntity1,
            BusinessPartnerVerboseValues.addressGateInputResponseSite1
        )

        val addresses = listOf(
            BusinessPartnerNonVerboseValues.addressGateInputRequest1,
            BusinessPartnerNonVerboseValues.addressGateInputRequest2
        )

        val legalEntity = listOf(
            BusinessPartnerNonVerboseValues.legalEntityGateInputRequest1
        )

        val site = listOf(
            BusinessPartnerNonVerboseValues.siteGateInputRequest1
        )

        val page = 0
        val size = 10

        val totalElements = 4L
        val totalPages = 1
        val pageValue = 0
        val contentSize = 2

        gateClient.legalEntities.upsertLegalEntities(legalEntity)
        gateClient.sites.upsertSites(site)
        gateClient.addresses.upsertAddresses(addresses)

        val paginationValue = PaginationRequest(page, size)
        val pageResponse = gateClient.addresses.getAddresses(paginationValue)

        val expectedPage = PageDto(
            totalElements = totalElements,
            totalPages = totalPages,
            page = pageValue,
            contentSize = contentSize,
            content = expectedAddresses
        )
        assertThat(pageResponse).usingRecursiveComparison().ignoringFieldsMatchingRegexes(
            ".*legalEntityExternalId*",
            ".*externalId*",
            ".*siteExternalId*",
            ".*totalElements*"
        ).isEqualTo(expectedPage)
    }


    /**
     * Given addresses exists in the Persistence Database
     * When getting addresses page based on external id list
     * Then addresses should be returned
     */
    @Test
    fun `get addresses filter by external ids`() {
        val addresses = listOf(
            BusinessPartnerNonVerboseValues.addressGateInputRequest1,
            BusinessPartnerNonVerboseValues.addressGateInputRequest2
        )

        val expectedAddresses = listOf(
            BusinessPartnerVerboseValues.logisticAddressGateInputResponse1,
            BusinessPartnerVerboseValues.logisticAddressGateInputResponse2,
        )

        val legalEntity = listOf(
            BusinessPartnerNonVerboseValues.legalEntityGateInputRequest1
        )

        val site = listOf(
            RequestValues.siteGateInputRequest1
        )

        val page = 0
        val size = 10

        val totalElements = 2L
        val totalPages = 1
        val pageValue = 0
        val contentSize = 2

        val listExternalIds = addresses.map { it.externalId }

        gateClient.legalEntities.upsertLegalEntities(legalEntity)
        gateClient.sites.upsertSites(site)
        gateClient.addresses.upsertAddresses(addresses)

        val pagination = PaginationRequest(page, size)
        val pageResponse = gateClient.addresses.getAddressesByExternalIds(pagination, listExternalIds)

        val expectedPage = PageDto(
            totalElements = totalElements,
            totalPages = totalPages,
            page = pageValue,
            contentSize = contentSize,
            content = expectedAddresses
        )
        assertThat(pageResponse).usingRecursiveComparison().ignoringFieldsMatchingRegexes(
            ".*legalEntityExternalId*",
            ".*siteExternalId*"
        ).isEqualTo(expectedPage)
    }

    /**
     * When requesting too many addresses
     * Then a bad request response should be sent
     */
    @Test
    fun `get addresses, pagination limit exceeded`() {

        val page = 0
        val size = 999999

        val paginationRequest = PaginationRequest(page, size)

        try {
            gateClient.addresses.getAddresses(paginationRequest)
        } catch (e: WebClientResponseException) {
            assertEquals(HttpStatus.BAD_REQUEST, e.statusCode)
        }

    }

    /**
     * Given legal entities and sites
     * When upserting addresses of legal entities and sites or a new one
     * Then upsert addresses should be persisted on the database
     */
    @Test
    fun `upsert addresses`() {
        val addresses = listOf(
            BusinessPartnerNonVerboseValues.addressGateInputRequest1,
            BusinessPartnerNonVerboseValues.addressGateInputRequest2
        )

        val legalEntity = listOf(
            BusinessPartnerNonVerboseValues.legalEntityGateInputRequest1
        )

        val site = listOf(
            BusinessPartnerNonVerboseValues.siteGateInputRequest1
        )

        try {
            gateClient.legalEntities.upsertLegalEntities(legalEntity)
            gateClient.sites.upsertSites(site)
            gateClient.addresses.upsertAddresses(addresses)
        } catch (e: WebClientResponseException) {
            assertEquals(HttpStatus.OK, e.statusCode)
        }

        //Check if persisted Address data
        val addressExternal1 = gateClient.addresses.getAddressByExternalId(CommonValues.externalIdAddress1)
        assertNotEquals(addressExternal1, null)

        val addressExternal2 = gateClient.addresses.getAddressByExternalId(CommonValues.externalIdAddress2)
        assertNotEquals(addressExternal2, null)

    }

    /**
     * When upserting addresses
     * if both have both have the same externalId, "bad request" should show
     */
    @Test
    fun `upsert addresses, same externalid`() {
        val addresses = listOf(
            BusinessPartnerNonVerboseValues.addressGateInputRequest1,
            BusinessPartnerNonVerboseValues.addressGateInputRequest1
        )

        try {
            gateClient.addresses.upsertAddresses(addresses)
        } catch (e: WebClientResponseException) {
            assertEquals(HttpStatus.BAD_REQUEST, e.statusCode)
        }

    }

    /**
     * When upserting addresses of legal entities using a legal entity external id that does not exist
     * Then a bad request response should be sent
     */
    @Test
    fun `upsert addresses, legal entity parent not found`() {
        val addresses = listOf(
            BusinessPartnerNonVerboseValues.addressGateInputRequest1
        )

        try {
            gateClient.addresses.upsertAddresses(addresses)
        } catch (e: WebClientResponseException) {
            assertEquals(HttpStatus.NOT_FOUND, e.statusCode)
        }

    }

    /**
     * When upserting addresses of sites using a site external id that does not exist
     * Then a bad request response should be sent
     */
    @Test
    fun `upsert addresses, site parent not found`() {
        val addresses = listOf(
            BusinessPartnerNonVerboseValues.addressGateInputRequest2
        )

        try {
            gateClient.addresses.upsertAddresses(addresses)
        } catch (e: WebClientResponseException) {
            assertEquals(HttpStatus.NOT_FOUND, e.statusCode)
        }

    }

    /**
     * When upserting an address without reference to either a parent site or a parent legal entity
     * Then a bad request response should be sent
     */
    @Test
    fun `upsert address without any parent`() {
        val addresses = listOf(
            BusinessPartnerNonVerboseValues.addressGateInputRequest1.copy(
                siteExternalId = null,
                legalEntityExternalId = null
            )
        )

        try {
            gateClient.addresses.upsertAddresses(addresses)
        } catch (e: WebClientResponseException) {
            assertEquals(HttpStatus.BAD_REQUEST, e.statusCode)
        }

    }

    /**
     * When upserting an address without reference to both a parent site and a parent legal entity
     * Then a bad request response should be sent
     */
    @Test
    fun `upsert address with site and legal entity parents`() {
        val addresses = listOf(
            BusinessPartnerNonVerboseValues.addressGateInputRequest1.copy(
                siteExternalId = BusinessPartnerVerboseValues.externalIdSite1,
                legalEntityExternalId = BusinessPartnerVerboseValues.externalId1
            )
        )

        try {
            gateClient.addresses.upsertAddresses(addresses)
        } catch (e: WebClientResponseException) {
            assertEquals(HttpStatus.BAD_REQUEST, e.statusCode)
        }

    }

    /**
     * When upserting an address without a persisted parent legal entity
     * Then a bad request response should be sent
     */
    @Test
    fun `upsert address with no parent legal entity`() {
        val addresses = listOf(
            BusinessPartnerNonVerboseValues.addressGateInputRequest1,
        )

        try {
            gateClient.addresses.upsertAddresses(addresses)
        } catch (e: WebClientResponseException) {
            assertEquals(HttpStatus.NOT_FOUND, e.statusCode)
        }

    }
}