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

import com.neovisionaries.i18n.CurrencyCode
import jakarta.persistence.*
import org.eclipse.tractusx.bpdm.common.entity.BaseEntity

@Entity
@Table(
    name = "bank_accounts",
    indexes = [
        Index(columnList = "legal_entity_id")
    ])
class BankAccountGate (
    @ElementCollection
    @JoinTable(name = "bank_account_trust_scores", joinColumns = [JoinColumn(name = "account_id")], indexes = [Index(columnList = "account_id")])
    @Column(name = "score", nullable = false)
    val trustScores: MutableSet<Float>,
    @Column(name = "currency", nullable = false)
    @Enumerated(EnumType.STRING)
    val currency: CurrencyCode,
    @Column(name = "international_account_identifier")
    val internationalBankAccountIdentifier: String?,
    @Column(name = "international_bank_identifier")
    val internationalBankIdentifier: String?,
    @Column(name = "national_account_identifier")
    val nationalBankAccountIdentifier: String?,
    @Column(name = "national_bank_identifier")
    val nationalBankIdentifier: String?,

    @ManyToOne
    @JoinColumn(name = "legal_entity_id", nullable = false)
    var legalEntity: LegalEntityGate
) : BaseEntity()