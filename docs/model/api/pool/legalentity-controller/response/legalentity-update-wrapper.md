````mermaid
classDiagram

LogisticAddressResponse ..> AddressStateResponse
LogisticAddressResponse ..> IdentifierResponse
LogisticAddressResponse ..> PhysicalPostalAddress
LogisticAddressResponse ..> AlternativePostalAddress
AlternativePostalAddress ..> GeoCoordinates
ClassificationResponse ..> TypeKeyNameDtoClassificationType
IdentifierResponse ..> TypeKeyNameDtoString
LegalEntityIdentifierResponse ..> TypeKeyNameDtoString
LegalEntityPartnerCreateResponse ..> LegalEntityIdentifierResponse
LegalEntityPartnerCreateResponse ..> LegalFormResponse
LegalEntityPartnerCreateResponse ..> LegalEntityStateResponse
LegalEntityPartnerCreateResponse ..> ClassificationResponse
LegalEntityPartnerCreateResponse ..> RelationResponse
LegalEntityPartnerCreateResponse ..> LogisticAddressResponse
LegalEntityStateResponse ..> TypeKeyNameDtoBusinessStateType
LegalEntityUpdateWrapper ..> LegalEntityPartnerCreateResponse
LegalEntityUpdateWrapper ..> ErrorInfoLegalEntityUpdateError
PhysicalPostalAddress ..> GeoCoordinates
PhysicalPostalAddress ..> Street
RelationResponse ..> TypeKeyNameDtoRelationType
AddressStateResponse ..> TypeKeyNameDtoBusinessStateType

class LogisticAddressResponse {
    + string: bpna
    + string: name
    + string: bpnLegalEntity
    + string: bpnSite
    + date: createdAt
    + date: updatedAt
    + bool: isLegalAddress
    + bool: isMainAddress
    + AddressStateResponse[]: states;
    + IdentifierResponse[]: identifiers;
    + PhysicalPostalAddress: physicalPostalAddress;
    + AlternativePostalAddress: alternativePostalAddress;
}

class AlternativePostalAddress {
    + string: country
    + string: postalCode
    + string: city
    + string: administrativeAreaLevel1
    + string: deliveryServiceNumber
    + string: deliveryServiceType
    + string: deliveryServiceQualifier
    + GeoCoordinates: geographicCoordinates;
}

class ClassificationResponse {
    + string: value
    + string: code
    + TypeKeyNameDtoClassificationType: type;
}

class IdentifierResponse {
    + string: value
    + TypeKeyNameDtoString: type;
}

class LegalEntityIdentifierResponse {
    + string: value
    + string: issuingBody
    + TypeKeyNameDtoString: type;
}

class LegalEntityPartnerCreateResponse {
    + string: legalName
    + string: bpnl
    + string: legalShortName
    + date: currentness
    + date: createdAt
    + date: updatedAt
    + string: index
    + LegalEntityIdentifierResponse[]: identifiers;
    + LegalFormResponse: legalForm;
    + LegalEntityStateResponse[]: states;
    + ClassificationResponse[]: classifications;
    + RelationResponse[]: relations;
    + LogisticAddressResponse: legalAddress;
}

class LegalEntityStateResponse {
    + string: officialDenotation
    + date: validFrom
    + date: validTo
    + TypeKeyNameDtoBusinessStateType: type;
}

class LegalEntityUpdateWrapper {
    + number: errorCount
    + number: entityCount
    + LegalEntityPartnerCreateResponse[]: entities;
    + ErrorInfoLegalEntityUpdateError[]: errors;
}

class PhysicalPostalAddress {
    + string: country
    + string: postalCode
    + string: city
    + string: administrativeAreaLevel1
    + string: administrativeAreaLevel2
    + string: administrativeAreaLevel3
    + string: district
    + string: companyPostalCode
    + string: industrialZone
    + string: building
    + string: floor
    + string: door
    + GeoCoordinates: geographicCoordinates;
    + Street: street;
}

class RelationResponse {
    + string: startBpn
    + string: endBpn
    + date: validFrom
    + date: validTo
    + TypeKeyNameDtoRelationType: type;
}

class AddressStateResponse {
    + string: description
    + date: validFrom
    + date: validTo
    + TypeKeyNameDtoBusinessStateType: type;
}

class LegalFormResponse{
+string: technicalKey
+string: name
+string: abbreviation
}

class TypeKeyNameDtoString{
+string: technicalKey
+string: name
}

class TypeKeyNameDtoBusinessStateType{
+string: technicalKey
+string: name
}

class TypeKeyNameDtoClassificationType{
+string: technicalKey
+string: name
}

class TypeKeyNameDtoRelationType{
+string: technicalKey
+string: name
}

class ErrorInfoLegalEntityUpdateError{
+string: errorCode
+string: message
+string: entityKey
}

class GeoCoordinates{
+number: longitude
+number: latitude
+number: altitude
}

class Street{
+string: name
+string: houseNumber
+string: milestone
+string: direction
}
````