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

package org.eclipse.tractusx.orchestrator.api.model

import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "Describes an error that happened during processing of a task")
data class TaskError(

    @get:Schema(description = "The type of error that occurred", required = true)
    val type: TaskErrorType,

    @get:Schema(description = "The free text, detailed description of the error", required = true)
    val description: String
)
