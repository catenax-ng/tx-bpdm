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

object LogisticAddressDescription {
    const val header = "In general, an address is a collection of information to describe a physical location, using a " +
            "street name with a house number and/or a post office box as reference. In addition, an " +
            "address consists of several postal attributes, such as country, region (state), county, township, " +
            "city, district, or postal code, which help deliver mail." +
            "In Catena-X, an address is a type of business partner representing the legal address of a legal " +
            "entity, and/or the main address of a site, or any additional address of a legal entity or site " +
            "(such as different gates)." +
            "An address is owned by a legal entity. Thus, exactly one legal entity is assigned to an address. " +
            "An address can belong to a site. Thus, one or no site is assigned to an address. An address is " +
            "uniquely identified by the BPNA."

    const val bpna = "A BPNA represents and uniquely identifies an address, which can be the legal address of a legal entity, " +
            "and/or the main address of a site, or any additional address of a legal entity or site (such as different gates). " +
            "It is important to note that only the BPNL must be used to uniquely identify a legal entity. " +
            "Even in the case that the BPNA represents the legal address of the legal entity, it shall not be used to uniquely identify the legal entity."
    const val name = "The name of the address. This is not according to official registers but according to the name the sharing member chooses."
    const val states = "The list of (temporary) states of the address."
    const val identifiers = "The list of identifiers of the address."
    const val physicalPostalAddress = "The physical postal address of the address, such as an office, warehouse, gate, etc."
    const val alternativePostalAddress = "The alternative postal address of the address, for example if the goods are to be picked up somewhere else."
    const val bpnLegalEntity = "The BPNL of the legal entity owning the address."
    const val isLegalAddress = "Indicates if the address is the legal address to a legal entity."
    const val bpnSite = "The BPNS of the site the address belongs to."
    const val isMainAddress = "Indicates if the address is the main address to a site. " +
            "This is where typically the main entrance or the reception is located, or where the mail is delivered to."
}