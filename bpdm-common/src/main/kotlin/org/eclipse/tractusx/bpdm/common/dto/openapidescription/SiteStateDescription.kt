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

package org.eclipse.tractusx.bpdm.common.dto.openapidescription

object SiteStateDescription {
    const val header = "A site state indicates if the site is active or inactive. This does not describe the relation " +
            "between a sharing member and a business partner and whether they have active business, " +
            "but it describes whether the site is still operating."

    const val description = "The description from the original source indicating the state of the site."
    const val validFrom = "The date from which the state is valid."
    const val validTo = "The date until the state is valid."
    const val type = "One of the state types: active, inactive."
}