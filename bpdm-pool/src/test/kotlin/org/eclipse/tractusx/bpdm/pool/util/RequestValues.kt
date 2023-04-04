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

package org.eclipse.tractusx.bpdm.pool.util

import com.neovisionaries.i18n.LanguageCode
import org.eclipse.tractusx.bpdm.common.dto.*
import org.eclipse.tractusx.bpdm.common.dto.response.type.TypeKeyNameDto
import org.eclipse.tractusx.bpdm.pool.api.model.request.*

object RequestValues {

    val name1 = NameDto(value = CommonValues.name1, shortName = null)
    val name2 = NameDto(value = CommonValues.name2, shortName = null)
    val name3 = NameDto(value = CommonValues.name3, shortName = null)
    val name4 = NameDto(value = CommonValues.name4, shortName = null)
    val name5 = NameDto(value = CommonValues.name5, shortName = null)

    val identifierType1 = TypeKeyNameDto(CommonValues.identifierTypeTechnicalKey1, CommonValues.identifierTypeName1)
    val identifierType2 = TypeKeyNameDto(CommonValues.identifierTypeTechnicalKey2, CommonValues.identifierTypeName2)
    val identifierType3 = TypeKeyNameDto(CommonValues.identifierTypeTechnicalKey3, CommonValues.identifierTypeName3)

    val identifier1 = IdentifierDto(
        CommonValues.identifierValue1,
        CommonValues.identifierTypeTechnicalKey1,
        CommonValues.issuingBody1,
    )

    val identifier2 = IdentifierDto(
        CommonValues.identifierValue2,
        CommonValues.identifierTypeTechnicalKey2,
        CommonValues.issuingBody2,
    )

    val identifier3 = IdentifierDto(
        CommonValues.identifierValue3,
        CommonValues.identifierTypeTechnicalKey3,
        CommonValues.issuingBody3,
    )

    val legalForm1 = LegalFormRequest(
        CommonValues.legalFormTechnicalKey1,
        CommonValues.legalFormName1,
        "TODO remove",
        CommonValues.legalFormAbbreviation1,
        LanguageCode.en
    )
    val legalForm2 = LegalFormRequest(
        CommonValues.legalFormTechnicalKey2,
        CommonValues.legalFormName2,
        "TODO remove",
        CommonValues.legalFormAbbreviation2,
        LanguageCode.de
    )
    val legalForm3 = LegalFormRequest(
        CommonValues.legalFormTechnicalKey3,
        CommonValues.legalFormName3,
        "TODO remove",
        CommonValues.legalFormAbbreviation3,
        LanguageCode.zh
    )

    val leStatus1 = LegalEntityStatusDto(CommonValues.statusDenotation1, CommonValues.statusValidFrom1, null, CommonValues.statusType1)
    val leStatus2 = LegalEntityStatusDto(CommonValues.statusDenotation2, CommonValues.statusValidFrom2, null, CommonValues.statusType2)
    val leStatus3 = LegalEntityStatusDto(CommonValues.statusDenotation3, CommonValues.statusValidFrom3, null, CommonValues.statusType3)

    val siteStatus1 = SiteStatusDto(CommonValues.statusDenotation1, CommonValues.statusValidFrom1, null, CommonValues.statusType1)
    val siteStatus2 = SiteStatusDto(CommonValues.statusDenotation2, CommonValues.statusValidFrom2, null, CommonValues.statusType2)
    val siteStatus3 = SiteStatusDto(CommonValues.statusDenotation3, CommonValues.statusValidFrom3, null, CommonValues.statusType3)

    val classification1 = ClassificationDto(CommonValues.classification1, null, CommonValues.classificationType)
    val classification2 = ClassificationDto(CommonValues.classification2, null, CommonValues.classificationType)
    val classification3 = ClassificationDto(CommonValues.classification3, null, CommonValues.classificationType)
    val classification4 = ClassificationDto(CommonValues.classification4, null, CommonValues.classificationType)
    val classification5 = ClassificationDto(CommonValues.classification5, null, CommonValues.classificationType)

    val adminArea1 = AdministrativeAreaDto(value = CommonValues.adminArea1, type = CommonValues.adminAreaType1)
    val adminArea2 = AdministrativeAreaDto(value = CommonValues.adminArea2, type = CommonValues.adminAreaType1)
    val adminArea3 = AdministrativeAreaDto(value = CommonValues.adminArea3, type = CommonValues.adminAreaType1)
    val adminArea4 = AdministrativeAreaDto(value = CommonValues.adminArea4, type = CommonValues.adminAreaType1)
    val adminArea5 = AdministrativeAreaDto(value = CommonValues.adminArea5, type = CommonValues.adminAreaType1)

    val postCode1 = PostCodeDto(CommonValues.postCode1, CommonValues.postCodeType1)
    val postCode2 = PostCodeDto(CommonValues.postCode2, CommonValues.postCodeType1)
    val postCode3 = PostCodeDto(CommonValues.postCode3, CommonValues.postCodeType1)
    val postCode4 = PostCodeDto(CommonValues.postCode4, CommonValues.postCodeType1)
    val postCode5 = PostCodeDto(CommonValues.postCode5, CommonValues.postCodeType1)

    val locality1 = LocalityDto(CommonValues.locality1, null, CommonValues.localityType1)
    val locality2 = LocalityDto(CommonValues.locality2, null, CommonValues.localityType1)
    val locality3 = LocalityDto(CommonValues.locality3, null, CommonValues.localityType1)
    val locality4 = LocalityDto(CommonValues.locality4, null, CommonValues.localityType1)
    val locality5 = LocalityDto(CommonValues.locality5, null, CommonValues.localityType1)

    val thoroughfare1 = ThoroughfareDto(value = CommonValues.thoroughfare1, type = CommonValues.thoroughfareType1)
    val thoroughfare2 = ThoroughfareDto(value = CommonValues.thoroughfare2, type = CommonValues.thoroughfareType1)
    val thoroughfare3 = ThoroughfareDto(value = CommonValues.thoroughfare3, type = CommonValues.thoroughfareType1)
    val thoroughfare4 = ThoroughfareDto(value = CommonValues.thoroughfare4, type = CommonValues.thoroughfareType1)
    val thoroughfare5 = ThoroughfareDto(value = CommonValues.thoroughfare5, type = CommonValues.thoroughfareType1)

    val premise1 = PremiseDto(value = CommonValues.premise1, type = CommonValues.premiseType1)
    val premise2 = PremiseDto(value = CommonValues.premise2, type = CommonValues.premiseType1)
    val premise3 = PremiseDto(value = CommonValues.premise3, type = CommonValues.premiseType1)
    val premise4 = PremiseDto(value = CommonValues.premise4, type = CommonValues.premiseType1)
    val premise5 = PremiseDto(value = CommonValues.premise5, type = CommonValues.premiseType1)

    val postalDeliveryPoint1 = PostalDeliveryPointDto(value = CommonValues.postalDeliveryPoint1, type = CommonValues.postalDeliveryPointType1)
    val postalDeliveryPoint2 = PostalDeliveryPointDto(value = CommonValues.postalDeliveryPoint2, type = CommonValues.postalDeliveryPointType1)
    val postalDeliveryPoint3 = PostalDeliveryPointDto(value = CommonValues.postalDeliveryPoint3, type = CommonValues.postalDeliveryPointType1)
    val postalDeliveryPoint4 = PostalDeliveryPointDto(value = CommonValues.postalDeliveryPoint4, type = CommonValues.postalDeliveryPointType1)
    val postalDeliveryPoint5 = PostalDeliveryPointDto(value = CommonValues.postalDeliveryPoint5, type = CommonValues.postalDeliveryPointType1)

    val version1 = AddressVersionDto(CommonValues.characterSet1, CommonValues.language0)

    val address1 = AddressDto(
        version = version1,
        country = CommonValues.country1,
        administrativeAreas = listOf(adminArea1, adminArea2),
        postCodes = listOf(postCode1, postCode2),
        localities = listOf(locality1, locality2),
        thoroughfares = listOf(thoroughfare1, thoroughfare2),
        premises = listOf(premise1, premise2),
        postalDeliveryPoints = listOf(postalDeliveryPoint1, postalDeliveryPoint2)
    )

    val address2 = AddressDto(
        version = version1,
        country = CommonValues.country2,
        administrativeAreas = listOf(adminArea3, adminArea4),
        postCodes = listOf(postCode3, postCode4),
        localities = listOf(locality3, locality4),
        thoroughfares = listOf(thoroughfare3, thoroughfare4),
        premises = listOf(premise3, premise4),
        postalDeliveryPoints = listOf(postalDeliveryPoint3, postalDeliveryPoint4)
    )

    val address3 = AddressDto(
        version = version1,
        country = CommonValues.country3,
        administrativeAreas = listOf(adminArea5),
        postCodes = listOf(postCode5),
        localities = listOf(locality5),
        thoroughfares = listOf(thoroughfare5),
        premises = listOf(premise5),
        postalDeliveryPoints = listOf(postalDeliveryPoint5)
    )


    val legalEntityCreate1 = LegalEntityPartnerCreateRequest(
        properties = LegalEntityDto(
            legalName = name1,
            identifiers = listOf(identifier1),
            legalForm = CommonValues.legalFormTechnicalKey1,
            status = listOf(leStatus1),
            classifications = listOf(classification1, classification2),
            legalAddress = address1
        ),
        index = CommonValues.index1
    )

    val legalEntityCreate2 = LegalEntityPartnerCreateRequest(
        properties = LegalEntityDto(
            legalName = name3,
            identifiers = listOf(identifier2),
            legalForm = CommonValues.legalFormTechnicalKey2,
            status = listOf(leStatus2),
            classifications = listOf(classification3, classification4),
            legalAddress = address2
        ),
        index = CommonValues.index2
    )

    val legalEntityCreate3 = LegalEntityPartnerCreateRequest(
        properties = LegalEntityDto(
            legalName = name5,
            identifiers = listOf(identifier3),
            legalForm = CommonValues.legalFormTechnicalKey3,
            status = listOf(leStatus3),
            classifications = listOf(classification5),
            legalAddress = address3
        ),
        index = CommonValues.index3
    )

    val legalEntityUpdate1 = LegalEntityPartnerUpdateRequest(
        bpn = CommonValues.bpnL1,
        properties = legalEntityCreate1.properties
    )

    val legalEntityUpdate2 = LegalEntityPartnerUpdateRequest(
        bpn = CommonValues.bpnL2,
        properties = legalEntityCreate2.properties
    )

    val legalEntityUpdate3 = LegalEntityPartnerUpdateRequest(
        bpn = CommonValues.bpnL3,
        properties = legalEntityCreate3.properties
    )

    val siteCreate1 = SitePartnerCreateRequest(
        site = SiteDto(
            name = CommonValues.siteName1,
            status = listOf(siteStatus1),
            mainAddress = address1
        ),
        index = CommonValues.index1,
        legalEntity = legalEntityUpdate1.bpn
    )

    val siteCreate2 = SitePartnerCreateRequest(
        site = SiteDto(
            name = CommonValues.siteName2,
            status = listOf(siteStatus2),
            mainAddress = address2
        ),
        index = CommonValues.index2,
        legalEntity = legalEntityUpdate2.bpn
    )

    val siteCreate3 = SitePartnerCreateRequest(
        site = SiteDto(
            name = CommonValues.siteName3,
            status = listOf(siteStatus3),
            mainAddress = address3
        ),
        index = CommonValues.index3,
        legalEntity = legalEntityUpdate3.bpn
    )

    val siteUpdate1 = SitePartnerUpdateRequest(
        bpn = CommonValues.bpnS1,
        site = siteCreate1.site
    )

    val siteUpdate2 = SitePartnerUpdateRequest(
        bpn = CommonValues.bpnS2,
        site = siteCreate2.site
    )

    val siteUpdate3 = SitePartnerUpdateRequest(
        bpn = CommonValues.bpnS3,
        site = siteCreate3.site
    )

    val addressPartnerCreate1 = AddressPartnerCreateRequest(
        properties = address1,
        parent = legalEntityUpdate1.bpn,
        index = CommonValues.index1
    )

    val addressPartnerCreate2 = AddressPartnerCreateRequest(
        properties = address2,
        parent = legalEntityUpdate2.bpn,
        index = CommonValues.index2
    )

    val addressPartnerCreate3 = AddressPartnerCreateRequest(
        properties = address3,
        parent = legalEntityUpdate3.bpn,
        index = CommonValues.index3
    )

    val addressPartnerUpdate1 = AddressPartnerUpdateRequest(
        bpn = CommonValues.bpnA1,
        properties = address1
    )

    val addressPartnerUpdate2 = AddressPartnerUpdateRequest(
        bpn = CommonValues.bpnA2,
        properties = address2
    )

    val addressPartnerUpdate3 = AddressPartnerUpdateRequest(
        bpn = CommonValues.bpnA3,
        properties = address3
    )

    val partnerStructure1 = LegalEntityStructureRequest(
        legalEntityCreate1,
        listOf(SiteStructureRequest(siteCreate1, listOf(addressPartnerCreate1)))
    )
}