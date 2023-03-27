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

package org.eclipse.tractusx.bpdm.gate.entity

import jakarta.persistence.*
import org.eclipse.tractusx.bpdm.common.entity.BaseEntity
import org.eclipse.tractusx.bpdm.common.model.BusinessPartnerType
import java.time.Instant

@Entity
@Table(
    name = "legal_entities",
    indexes = [Index(columnList = "legal_form_id")]
)
class LegalEntityGate(
    @Column(name = "bpn", nullable = false, unique = true)
    var bpn: String,
    @ManyToOne
    @JoinColumn(name = "legal_form_id")
    var legalForm: LegalFormGate?,
    @ElementCollection(targetClass = BusinessPartnerType::class)
    @JoinTable(name = "legal_entity_types", joinColumns = [JoinColumn(name = "legal_entity_id")], indexes = [Index(columnList = "legal_entity_id")])
    @Column(name = "type", nullable = false)
    @Enumerated(EnumType.STRING)
    var types: MutableSet<BusinessPartnerType>,
    @ManyToMany(cascade = [CascadeType.ALL])
    @JoinTable(
        name = "legal_entity_roles",
        joinColumns = [JoinColumn(name = "legal_entity_id")],
        inverseJoinColumns = [JoinColumn(name = "role_id")],
        indexes = [Index(columnList = "legal_entity_id")]
    )
    val roles: MutableSet<RoleGate>,
    @Column(name = "currentness", nullable = false)
    var currentness: Instant,
    @ManyToOne(fetch = FetchType.LAZY, cascade = [CascadeType.ALL])
    @JoinColumn(name = "legal_address_id", nullable = false)
    var legalAddress: AddressGate,
    @Column(name = "external_id", nullable = false, unique = true)
    var externalId: String,
) : BaseEntity() {
    @OneToMany(mappedBy = "legalEntity", cascade = [CascadeType.ALL], orphanRemoval = true)
    val identifiers: MutableSet<IdentifierGate> = mutableSetOf()

    @OneToMany(mappedBy = "legalEntity", cascade = [CascadeType.ALL], orphanRemoval = true)
    val nameGates: MutableSet<NameGate> = mutableSetOf()

    @OneToMany(mappedBy = "legalEntity", cascade = [CascadeType.ALL], orphanRemoval = true)
    val stati: MutableSet<BusinessStatusGate> = mutableSetOf()

    @OneToMany(mappedBy = "legalEntity", cascade = [CascadeType.ALL], orphanRemoval = true)
    val addresses: MutableSet<AddressPartnerGate> = mutableSetOf()

//    @OneToMany(mappedBy = "legalEntity", cascade = [CascadeType.ALL], orphanRemoval = true)
//    val sites: MutableSet<SiteGate> = mutableSetOf()

    @OneToMany(mappedBy = "legalEntity", cascade = [CascadeType.ALL], orphanRemoval = true)
    val classification: MutableSet<ClassificationGate> = mutableSetOf()

    @OneToMany(mappedBy = "legalEntity", cascade = [CascadeType.ALL], orphanRemoval = true)
    val bankAccounts: MutableSet<BankAccountGate> = mutableSetOf()

    @OneToMany(mappedBy = "startNode", cascade = [CascadeType.ALL], orphanRemoval = true)
    val startNodeRelations: MutableSet<RelationGate> = mutableSetOf()

    @OneToMany(mappedBy = "endNode", cascade = [CascadeType.ALL], orphanRemoval = true)
    val endNodeRelations: MutableSet<RelationGate> = mutableSetOf()
}

