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
import org.eclipse.tractusx.bpdm.gate.dto.LegalEntityGateInputRequest
import org.eclipse.tractusx.bpdm.gate.entity.*
import java.time.Instant
import java.time.temporal.ChronoUnit

//Legal Entity
fun LegalEntityGateInputRequest.toLegalEntityGate(): LegalEntityGate{

    val legalEntityGate = LegalEntityGate(
        bpn = bpn.toString(),
        legalForm = legalEntity.legalForm.toString(),
        currentness = createCurrentnessTimestamp(),
        externalId = externalId.toString(),
        types = legalEntity.types.toMutableSet(),
        legalAddress = legalEntity.legalAddress.toAddressGateDto(),
        roles = mutableSetOf()
    )

    legalEntityGate.identifiers.addAll(this.legalEntity.identifiers.map { toEntity(it, legalEntityGate) }.toSet())
    legalEntityGate.nameGates.addAll(this.legalEntity.names.map {toEntity(it, legalEntityGate)}.toSet())
    legalEntityGate.bankAccounts.addAll(this.legalEntity.bankAccounts.map { toEntity(it,legalEntityGate) }.toSet())
    legalEntityGate.classification.addAll(this.legalEntity.profileClassifications.map { toEntity(it, legalEntityGate) }.toSet())
    legalEntityGate.stati.addAll(if (this.legalEntity.status != null) setOf(toEntity(this.legalEntity.status!!, legalEntityGate)) else setOf())
    return legalEntityGate;
}

private fun createCurrentnessTimestamp(): Instant {
    return Instant.now().truncatedTo(ChronoUnit.MICROS)
}

fun toEntity(dto: IdentifierDto, legalEntityGate: LegalEntityGate): IdentifierGate {
    return IdentifierGate(
        dto.value, IdentifierTypeGate(dto.type, "", ""),
        IdentifierStatusGate(dto.status.toString(), ""),
        IssuingBodyGate(dto.issuingBody.toString(), "", ""), legalEntityGate
    )
}

fun toEntity(dto: NameDto, legalEntityGate: LegalEntityGate): NameGate {
    return NameGate(dto.value, dto.shortName, dto.type, dto.language, legalEntityGate)
}

fun toEntity(dto: BankAccountDto, legalEntityGate: LegalEntityGate): BankAccountGate {
    return BankAccountGate(
        dto.trustScores.toMutableSet(), currency = dto.currency,
        dto.nationalBankAccountIdentifier.toString(), dto.internationalBankIdentifier.toString(),
        dto.nationalBankAccountIdentifier.toString(), dto.nationalBankIdentifier.toString(), legalEntityGate
    )
}

fun toEntity(dto: ClassificationDto, legalEntityGate: LegalEntityGate): ClassificationGate {
    return ClassificationGate(dto.value, dto.code, dto.type, legalEntityGate)
}

private fun toEntity(dto: BusinessStatusDto, partner: LegalEntityGate): BusinessStatusGate {
    return BusinessStatusGate(dto.officialDenotation, dto.validFrom, dto.validUntil, dto.type, partner)
}


