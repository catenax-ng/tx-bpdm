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

package org.eclipse.tractusx.bpdm.pool.entity

import jakarta.persistence.*
import org.eclipse.tractusx.bpdm.common.model.BaseEntity
import java.time.Instant

@Entity
@Table(
    name = "legal_entities",
    indexes = [Index(columnList = "legal_form_id")]
)
class LegalEntityDb(
    @Column(name = "bpn", nullable = false, unique = true)
    var bpn: String,

    @Embedded
    var legalName: NameDb,

    @ManyToOne
    @JoinColumn(name = "legal_form_id")
    var legalForm: LegalFormDb?,

    @Column(name = "currentness", nullable = false)
    var currentness: Instant,

    @Embedded
    var confidenceCriteria: ConfidenceCriteriaDb

) : BaseEntity() {
    @OneToMany(mappedBy = "legalEntity", cascade = [CascadeType.ALL], orphanRemoval = true)
    val identifiers: MutableSet<LegalEntityIdentifierDb> = mutableSetOf()

    @OneToMany(mappedBy = "legalEntity", cascade = [CascadeType.ALL], orphanRemoval = true)
    val states: MutableSet<LegalEntityStateDb> = mutableSetOf()

    @OneToMany(mappedBy = "legalEntity", cascade = [CascadeType.ALL], orphanRemoval = true)
    val addresses: MutableSet<LogisticAddressDb> = mutableSetOf()

    @OneToMany(mappedBy = "legalEntity", cascade = [CascadeType.ALL], orphanRemoval = true)
    val sites: MutableSet<SiteDb> = mutableSetOf()

    @OneToMany(mappedBy = "legalEntity", cascade = [CascadeType.ALL], orphanRemoval = true)
    val classifications: MutableSet<LegalEntityClassificationDb> = mutableSetOf()

    @OneToMany(mappedBy = "startNode", cascade = [CascadeType.ALL], orphanRemoval = true)
    val startNodeRelations: MutableSet<RelationDb> = mutableSetOf()

    @OneToMany(mappedBy = "endNode", cascade = [CascadeType.ALL], orphanRemoval = true)
    val endNodeRelations: MutableSet<RelationDb> = mutableSetOf()

    @ManyToOne(fetch = FetchType.LAZY, cascade = [CascadeType.ALL])
    @JoinColumn(name = "legal_address_id", nullable = false)
    lateinit var legalAddress: LogisticAddressDb
}
