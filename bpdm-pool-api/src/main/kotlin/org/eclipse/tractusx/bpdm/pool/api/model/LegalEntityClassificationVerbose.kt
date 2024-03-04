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

package org.eclipse.tractusx.bpdm.pool.api.model

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonProperty
import io.swagger.v3.oas.annotations.media.Schema
import org.eclipse.tractusx.bpdm.common.dto.ILegalEntityClassification
import org.eclipse.tractusx.bpdm.common.dto.TypeKeyNameVerbose
import org.eclipse.tractusx.bpdm.common.dto.openapidescription.ClassificationDescription
import org.eclipse.tractusx.bpdm.common.model.ClassificationType

@Schema(description = ClassificationDescription.header)
data class LegalEntityClassificationVerbose(

    override val value: String? = null,
    override val code: String? = null,

    @field:JsonProperty("type")
    @get:Schema(description = ClassificationDescription.type)
    val typeVerbose: TypeKeyNameVerbose<ClassificationType>

) : ILegalEntityClassification {

    @get:JsonIgnore
    override val type: ClassificationType
        get() = typeVerbose.technicalKey
}
