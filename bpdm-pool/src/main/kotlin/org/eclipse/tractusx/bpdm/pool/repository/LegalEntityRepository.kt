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

package org.eclipse.tractusx.bpdm.pool.repository

import org.eclipse.tractusx.bpdm.pool.entity.IdentifierTypeDb
import org.eclipse.tractusx.bpdm.pool.entity.LegalEntityDb
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.PagingAndSortingRepository
import java.time.Instant

interface LegalEntityRepository : PagingAndSortingRepository<LegalEntityDb, Long>, CrudRepository<LegalEntityDb, Long> {
    fun findByBpn(bpn: String): LegalEntityDb?

    fun existsByBpn(bpn: String): Boolean

    fun findDistinctByBpnIn(bpns: Collection<String>): Set<LegalEntityDb>

    fun findByUpdatedAtAfter(updatedAt: Instant, pageable: Pageable): Page<LegalEntityDb>

    @Query("SELECT p FROM LegalEntityDb p WHERE LOWER(p.legalName.value) LIKE :value ORDER BY LENGTH(p.legalName.value)")
    fun findByLegalNameValue(value: String, pageable: Pageable): Page<LegalEntityDb>

    @Query("SELECT DISTINCT i.legalEntity FROM LegalEntityIdentifierDb i WHERE i.type = :type AND upper(i.value) = upper(:idValue)")
    fun findByIdentifierTypeAndValueIgnoreCase(type: IdentifierTypeDb, idValue: String): LegalEntityDb?

    @Query("SELECT DISTINCT p FROM LegalEntityDb p LEFT JOIN FETCH p.legalForm WHERE p IN :partners")
    fun joinLegalForm(partners: Set<LegalEntityDb>): Set<LegalEntityDb>

    @Query("SELECT DISTINCT p FROM LegalEntityDb p LEFT JOIN FETCH p.identifiers WHERE p IN :partners")
    fun joinIdentifiers(partners: Set<LegalEntityDb>): Set<LegalEntityDb>

    @Query("SELECT DISTINCT p FROM LegalEntityDb p LEFT JOIN FETCH p.states WHERE p IN :partners")
    fun joinStates(partners: Set<LegalEntityDb>): Set<LegalEntityDb>

    @Query("SELECT DISTINCT p FROM LegalEntityDb p LEFT JOIN FETCH p.classifications WHERE p IN :partners")
    fun joinClassifications(partners: Set<LegalEntityDb>): Set<LegalEntityDb>

    @Query("SELECT DISTINCT p FROM LegalEntityDb p LEFT JOIN FETCH p.startNodeRelations LEFT JOIN FETCH p.endNodeRelations WHERE p IN :partners")
    fun joinRelations(partners: Set<LegalEntityDb>): Set<LegalEntityDb>

    @Query("SELECT DISTINCT p FROM LegalEntityDb p LEFT JOIN FETCH p.legalAddress WHERE p IN :partners")
    fun joinLegalAddresses(partners: Set<LegalEntityDb>): Set<LegalEntityDb>
}