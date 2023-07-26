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

package org.eclipse.tractusx.bpdm.gate.util

import com.fasterxml.jackson.databind.ObjectMapper
import com.neovisionaries.i18n.CountryCode
import org.apache.poi.ss.usermodel.*
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import org.eclipse.tractusx.bpdm.common.dto.*
import org.eclipse.tractusx.bpdm.gate.api.model.LogisticAddressGateDto
import org.eclipse.tractusx.bpdm.gate.api.model.PhysicalPostalAddressGateDto
import org.eclipse.tractusx.bpdm.gate.api.model.StreetGateDto
import org.eclipse.tractusx.bpdm.gate.api.model.request.LegalEntityGateInputRequest
import org.junit.jupiter.api.Test
import java.io.File
import java.io.FileInputStream

class MigrateDataIT {

    @Test
    fun convertData() {

        val convertedData: MigrationData =
            readExcelData("C:\\Project\\exxcellent\\catenax-ng\\bpdm-gate\\src\\test\\kotlin\\org\\eclipse\\tractusx\\bpdm\\gate\\util\\TestDsts.xlsx")

        val addressesById = convertedData.addresses.map { it.address_id to it }.toMap()
        val identifiersById = convertedData.identifiers.map { it.company_id to it }.toMap()

        val requests =
            convertedData.companies.map {
                convertTestdataToRequest(it, addressesById, identifiersById)
            }.filterNotNull()

        val targetFile = File("C:\\Project\\exxcellent\\catenax-ng\\bpdm-gate\\src\\test\\kotlin\\org\\eclipse\\tractusx\\bpdm\\gate\\util\\ConvertedData.json")
        val objectMapper = ObjectMapper()
        objectMapper.writeValue(targetFile, requests)
    }


    private fun convertTestdataToRequest(
        company: LegelEnityData,
        addressesById: Map<String, AddressData>,
        identifiersById: Map<String, IdentifierData>
    ): LegalEntityGateInputRequest? {

        val address = addressesById[company.address_id]
        val identifier = identifiersById[company.externalId]
        val identifiers: Collection<LegalEntityIdentifierDto> = emptyList()
        if (identifier != null) {
            LegalEntityIdentifierDto(value = identifier.value, type = identifier.unique_identifier_id, issuingBody = null)
        }

        if (address != null && company.bpnl.isNotBlank() && company.bpnl.startsWith("BPNL")) {

            val street = StreetGateDto(name = address.streetname, houseNumber = address.streetnumber)

            return LegalEntityGateInputRequest(
                legalNameParts = listOf(company.name),
                externalId = company.externalId, legalAddress = LogisticAddressGateDto(
                    physicalPostalAddress = PhysicalPostalAddressGateDto(
                        baseAddress = BasePostalAddressDto(city = address.city, country = address.countryCode, postalCode = address.zipcode),
                        areaPart = AreaDistrictDto(),
                        basePhysicalAddress = BasePhysicalAddressDto(),
                        street = street
                    )
                ), legalEntity = LegalEntityDto(legalShortName = company.shortname, identifiers = identifiers)
            )
        } else {
            return null;
        }

    }


    private fun readExcelData(fileName: String): MigrationData {

        FileInputStream(fileName).use { inp ->
            val workbook: Workbook = XSSFWorkbook(inp)
            val sheetIterator: Iterator<Sheet> = workbook.sheetIterator()
            val sheet: Sheet = workbook.getSheetAt(0)
            return MigrationData(
                companies = readLegalEntitiesFromSheet(workbook.getSheetAt(0)),
                addresses = readAddressesFromSheet(workbook.getSheetAt(1)),
                identifiers = readIdentifierFromSheet(workbook.getSheetAt(2))
            )
        }
    }

    private fun readLegalEntitiesFromSheet(sheet: Sheet): List<LegelEnityData> {
        val companies: MutableList<LegelEnityData> = mutableListOf()
        val rowIter = sheet.rowIterator()
        while (rowIter.hasNext()) {
            val row = rowIter.next()
            if (row.rowNum > 0) {
                companies.add(convertLegalEnityRow(row))
            }
        }
        return companies
    }

    private fun readAddressesFromSheet(sheet: Sheet): List<AddressData> {
        val addresses: MutableList<AddressData> = mutableListOf()
        val rowIter = sheet.rowIterator()
        while (rowIter.hasNext()) {
            val row = rowIter.next()
            if (row.rowNum > 0) {
                addresses.add(convertAddressRow(row))
            }
        }
        return addresses
    }

    private fun readIdentifierFromSheet(sheet: Sheet): List<IdentifierData> {
        val identifiers: MutableList<IdentifierData> = mutableListOf()
        val rowIter = sheet.rowIterator()
        while (rowIter.hasNext()) {
            val row = rowIter.next()
            if (row.rowNum > 0) {
                identifiers.add(convertIdentifierRow(row))
            }
        }
        return identifiers
    }


    private fun convertLegalEnityRow(row: Row): LegelEnityData {

        val companyId = getCellValueString(row.getCell(0))
        val business_partner_number = getCellValueString(row.getCell(2))
        val name = getCellValueString(row.getCell(3))
        val shortname = getCellValueString(row.getCell(4))
        val address_id = getCellValueString(row.getCell(6))
        return LegelEnityData(externalId = companyId, bpnl = business_partner_number, name = name, shortname = shortname, address_id = address_id)
    }

    private fun convertAddressRow(row: Row): AddressData {

        val address_id = getCellValueString(row.getCell(0))
        val city = getCellValueString(row.getCell(3))
        val streetname = getCellValueString(row.getCell(6))
        val zipcode = getCellValueString(row.getCell(7))
        val streetnumber = getCellValueString(row.getCell(8))
        val country_alpha2code = getCellValueString(row.getCell(9))
        return AddressData(
            address_id = address_id,
            city = city,
            streetname = streetname,
            zipcode = zipcode,
            streetnumber = streetnumber,
            countryCode = CountryCode.getByAlpha2Code(country_alpha2code)
        )
    }

    private fun convertIdentifierRow(row: Row): IdentifierData {

        val company_id = getCellValueString(row.getCell(0));
        val unique_identifier_id = getCellValueString(row.getCell(1));
        val value = getCellValueString(row.getCell(2));
        return IdentifierData(company_id = company_id, unique_identifier_id = unique_identifier_id, value = value)
    }


    private fun getCellValueString(cell: Cell?): String {
        return if (cell == null || cell.cellType == CellType.BLANK) {
            ""
        } else if (cell.cellType == CellType.NUMERIC) {
            (cell.numericCellValue).toInt().toString()
        } else {
            if (cell.stringCellValue.equals("NULL")) {
                return ""
            } else {
                cell.stringCellValue
            }
        }
    }

    private fun getCellValueDouble(cell: Cell?): Double? {
        return if (cell == null || cell.cellType == CellType.BLANK) {
            null
        } else if (cell.cellType == CellType.STRING) try {
            cell.stringCellValue.toDouble()
        } catch (ex: NumberFormatException) {
            null
        } else {
            cell.numericCellValue
        }
    }

    data class LegelEnityData(

        val externalId: String,
        val bpnl: String,
        val name: String,
        val shortname: String,
        val address_id: String,
    )

    data class AddressData(

        val address_id: String,
        val city: String,
        val streetname: String,
        val zipcode: String,
        val streetnumber: String,
        val countryCode: CountryCode = CountryCode.US
    )

    data class IdentifierData(

        val company_id: String,
        val unique_identifier_id: String,
        val value: String,
    )


    data class MigrationData(
        val companies: List<LegelEnityData>,
        val addresses: List<AddressData>,
        val identifiers: List<IdentifierData>
    )

}