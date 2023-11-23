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

package org.eclipse.tractusx.bpdm.gate.repository.generic

import org.eclipse.tractusx.bpdm.common.model.StageType
import org.eclipse.tractusx.bpdm.gate.entity.generic.BusinessPartner
import org.springframework.data.jpa.domain.Specification
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface BusinessPartnerRepository : JpaRepository<BusinessPartner, Long>, CrudRepository<BusinessPartner, Long>, JpaSpecificationExecutor<BusinessPartner> {

    object Specs {

        /**
         * Restrict to entries with the given `stage`; ignore if null
         */
        fun byStage(stage: StageType) =
            Specification<BusinessPartner> { root, _, builder ->
                builder.equal(root.get<StageType>(BusinessPartner::stage.name), stage)
            }

        /**
         * Restrict to entries with any one of the given `externalIds`; ignore if null or empty
         */
        fun byExternalIdsIn(externalIds: Collection<String>?) =
            Specification<BusinessPartner> { root, _, _ ->
                if (!externalIds.isNullOrEmpty())
                    root.get<String>(BusinessPartner::externalId.name).`in`(externalIds)
                else
                    null
            }
    }

    fun findByStageAndExternalIdIn(stage: StageType, externalId: Collection<String>): Set<BusinessPartner>

//    fun findByStageAndExternalIdIn(stage: StageType, externalId: Collection<String>, pageable: Pageable): Page<BusinessPartner>

//    fun findByStage(stage: StageType, pageable: Pageable): Page<BusinessPartner>
}
