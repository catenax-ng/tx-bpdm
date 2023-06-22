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

package org.eclipse.tractusx.bpdm.pool.util

import jakarta.persistence.EntityManager
import jakarta.persistence.EntityManagerFactory
import org.assertj.core.api.Assertions
import org.assertj.core.api.RecursiveComparisonAssert
import org.eclipse.tractusx.bpdm.common.model.SyncStatus
import org.eclipse.tractusx.bpdm.pool.api.client.PoolClientImpl
import org.eclipse.tractusx.bpdm.pool.api.model.request.IdentifiersSearchRequest
import org.eclipse.tractusx.bpdm.pool.api.model.response.ErrorCode
import org.eclipse.tractusx.bpdm.pool.api.model.response.ErrorInfo
import org.eclipse.tractusx.bpdm.pool.api.model.response.SyncDto
import org.eclipse.tractusx.bpdm.pool.config.BpnConfigProperties
import org.junit.Assert
import org.junit.jupiter.api.assertThrows
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.web.reactive.function.client.WebClientResponseException
import java.time.Instant


private const val ASYNC_TIMEOUT_IN_MS: Long = 5 * 1000 //5 seconds
private const val ASYNC_CHECK_INTERVAL_IN_MS: Long = 200
private const val BPDM_DB_SCHEMA_NAME: String = "bpdm"

@Component
class TestHelpers(
    entityManagerFactory: EntityManagerFactory,
    private val bpnConfigProperties: BpnConfigProperties,
    private val poolClient: PoolClientImpl
) {

    val bpnLPattern = createBpnPattern(bpnConfigProperties.legalEntityChar)
    val bpnSPattern = createBpnPattern(bpnConfigProperties.siteChar)
    val bpnAPattern = createBpnPattern(bpnConfigProperties.addressChar)


    val em: EntityManager = entityManagerFactory.createEntityManager()


    fun truncateDbTables() {
        em.transaction.begin()

        em.createNativeQuery(
            """
            DO $$ DECLARE table_names RECORD;
            BEGIN
                FOR table_names IN SELECT table_name
                    FROM information_schema.tables
                    WHERE table_schema='$BPDM_DB_SCHEMA_NAME'
                    AND table_name NOT IN ('flyway_schema_history') 
                LOOP 
                    EXECUTE format('TRUNCATE TABLE $BPDM_DB_SCHEMA_NAME.%I CONTINUE IDENTITY CASCADE;', table_names.table_name);
                END LOOP;
            END $$;
        """.trimIndent()
        ).executeUpdate()

        em.transaction.commit()
    }


    /**
     * Creates legal entities, sites and addresses according to the given [partnerStructures]
     * Retains the order: All response objects will be in the same order as their request counterparts
     * Assumption: Legal entities, sites and addresses have unique indexes among them each
     */
    fun createBusinessPartnerStructure(
        partnerStructures: List<LegalEntityStructureRequest>
    ): List<LegalEntityStructureResponse> {

        val legalEntities = poolClient.legalEntities().createBusinessPartners(partnerStructures.map { it.legalEntity })
        val indexedLegalEntities = legalEntities.entities.associateBy { it.index }

        val assignedSiteRequests =
            partnerStructures.flatMap {
                it.siteStructures.map { site ->
                    site.site.copy(bpnlParent = indexedLegalEntities[it.legalEntity.index]!!.legalEntity.bpnl)
                }
            }
        val sitesWithErrorsResponse = poolClient.sites().createSite(assignedSiteRequests)
        val indexedSites = sitesWithErrorsResponse.entities.associateBy { it.index }

        val assignedSitelessAddresses =
            partnerStructures.flatMap {
                it.addresses.map { address ->
                    address.copy(bpnParent = indexedLegalEntities[it.legalEntity.index]!!.legalEntity.bpnl)
                }
            }
        val assignedSiteAddresses =
            partnerStructures
                .flatMap { it.siteStructures }
                .flatMap {
                    it.addresses.map { address -> address.copy(bpnParent = indexedSites[it.site.index]!!.site.bpns) }
                }

        val addresses = poolClient.addresses().createAddresses(assignedSitelessAddresses + assignedSiteAddresses).entities

        val indexedAddresses = addresses.associateBy { it.index }

        return partnerStructures.map { legalEntityStructure ->
            LegalEntityStructureResponse(
                legalEntity = indexedLegalEntities[legalEntityStructure.legalEntity.index]!!,
                siteStructures = legalEntityStructure.siteStructures.map { siteStructure ->
                    SiteStructureResponse(
                        site = indexedSites[siteStructure.site.index]!!,
                        addresses = siteStructure.addresses.map { indexedAddresses[it.index]!! }
                    )
                },
                addresses = legalEntityStructure.addresses.map { indexedAddresses[it.index]!! }
            )
        }
    }

    fun `get address by bpn-a, not found`(bpn: String) {
        try {
            val result = poolClient.addresses().getAddress(bpn)
            assertThrows<WebClientResponseException> { result }
        } catch (e: WebClientResponseException) {
            Assert.assertEquals(HttpStatus.NOT_FOUND, e.statusCode)
        }
    }

    fun `find bpns by identifiers, bpn request limit exceeded`(identifiersSearchRequest: IdentifiersSearchRequest) {
        try {
            val result = poolClient.bpns().findBpnsByIdentifiers(identifiersSearchRequest)

            assertThrows<WebClientResponseException> { result }
        } catch (e: WebClientResponseException) {
            Assert.assertEquals(HttpStatus.BAD_REQUEST, e.statusCode)
        }
    }

    fun `find bpns by nonexistent identifier type`(identifiersSearchRequest: IdentifiersSearchRequest) {
        try {
            val result = poolClient.bpns().findBpnsByIdentifiers(identifiersSearchRequest)
            assertThrows<WebClientResponseException> { result }
        } catch (e: WebClientResponseException) {
            Assert.assertEquals(HttpStatus.NOT_FOUND, e.statusCode)
        }
    }

    fun `set business partner currentness using nonexistent bpn`(bpn: String) {
        try {
            val result = poolClient.legalEntities().setLegalEntityCurrentness(bpn)
            assertThrows<WebClientResponseException> { result }
        } catch (e: WebClientResponseException) {
            Assert.assertEquals(HttpStatus.NOT_FOUND, e.statusCode)
        }
    }

    fun `get site by bpn-s, not found`(bpn: String) {
        try {
            val result = poolClient.sites().getSite(bpn)
            assertThrows<WebClientResponseException> { result }
        } catch (e: WebClientResponseException) {
            Assert.assertEquals(HttpStatus.NOT_FOUND, e.statusCode)
        }
    }


    /**
     * Creates metadata needed for test data defined in the [RequestValues]
     */
    fun createTestMetadata() {

        poolClient.metadata().createLegalForm(RequestValues.legalForm1)
        poolClient.metadata().createLegalForm(RequestValues.legalForm2)
        poolClient.metadata().createLegalForm(RequestValues.legalForm3)

        poolClient.metadata().createIdentifierType(RequestValues.identifierTypeDto1)
        poolClient.metadata().createIdentifierType(RequestValues.identifierTypeDto2)
        poolClient.metadata().createIdentifierType(RequestValues.identifierTypeDto3)
    }


    fun startSyncAndAwaitSuccess(client: WebTestClient, syncPath: String): SyncDto {
        return startSyncAndAwaitResult(client, syncPath, SyncStatus.SUCCESS)
    }

    fun startSyncAndAwaitError(client: WebTestClient, syncPath: String): SyncDto {
        return startSyncAndAwaitResult(client, syncPath, SyncStatus.ERROR)
    }

    private fun startSyncAndAwaitResult(client: WebTestClient, syncPath: String, status: SyncStatus): SyncDto {

        client.invokePostEndpointWithoutResponse(syncPath)
        //check for async import to finish several times
        val timeOutAt = Instant.now().plusMillis(ASYNC_TIMEOUT_IN_MS)
        var syncResponse: SyncDto
        do {
            Thread.sleep(ASYNC_CHECK_INTERVAL_IN_MS)

            syncResponse = client.invokeGetEndpoint(syncPath)

            if (syncResponse.status == status)
                break

        } while (Instant.now().isBefore(timeOutAt))

        Assertions.assertThat(syncResponse.status).isEqualTo(status)

        return syncResponse
    }


    fun <T> assertRecursively(actual: T): RecursiveComparisonAssert<*> {
        return Assertions.assertThat(actual)
            .usingRecursiveComparison()
            .ignoringCollectionOrder()
            .ignoringAllOverriddenEquals()
            .ignoringFieldsOfTypes(Instant::class.java)
    }

    fun <ERROR : ErrorCode> assertErrorResponse(errorResponse: ErrorInfo<ERROR>, codeToCheck: ERROR, keyToCheck: String) {
        Assertions.assertThat(errorResponse.entityKey).isEqualTo(keyToCheck)
        Assertions.assertThat(errorResponse.errorCode).isEqualTo(codeToCheck)
    }

    private fun createBpnPattern(typeId: Char): String {
        return "${bpnConfigProperties.id}$typeId[${bpnConfigProperties.alphabet}]{${bpnConfigProperties.counterDigits + 2}}"
    }
}