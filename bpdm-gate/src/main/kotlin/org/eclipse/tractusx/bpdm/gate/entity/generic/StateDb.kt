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

package org.eclipse.tractusx.bpdm.gate.entity.generic

import jakarta.persistence.Column
import jakarta.persistence.Embeddable
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import org.eclipse.tractusx.bpdm.common.model.BusinessStateType
import java.time.LocalDateTime

@Embeddable
data class StateDb(

    @Column(name = "valid_from")
    var validFrom: LocalDateTime?,

    @Column(name = "valid_to")
    var validTo: LocalDateTime?,

    @Column(name = "type", nullable = false)
    @Enumerated(EnumType.STRING)
    var type: BusinessStateType

) : Comparable<StateDb> {

    // Natural order by "validFrom", "validTo", "type", "description"
    override fun compareTo(other: StateDb) =
        compareBy(nullsFirst(), StateDb::validFrom)       // here null means MIN
            .thenBy(nullsLast(), StateDb::validTo)        // here null means MAX
            .thenBy(StateDb::type)
            .compare(this, other)
}
