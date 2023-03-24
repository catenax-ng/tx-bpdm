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

@Entity
@Table(
    name = "identifiers",
    indexes = [
        Index(columnList = "legal_entity_id"),
        Index(columnList = "type_id"),
        Index(columnList = "status"),
        Index(columnList = "issuing_body_id")
    ]
)
class IdentifierGate(
    @Column(name = "`value`", nullable = false)
    var value: String,
    @ManyToOne
    @JoinColumn(name = "type_id", nullable = false)
    var type: IdentifierTypeGate,
    @ManyToOne
    @JoinColumn(name = "status")
    var status: IdentifierStatusGate?,
    @ManyToOne
    @JoinColumn(name = "issuing_body_id")
    var issuingBody: IssuingBodyGate?,
    @ManyToOne
    @JoinColumn(name = "legal_entity_id", nullable = false)
    var legalEntity: LegalEntityGate
) : BaseEntity()