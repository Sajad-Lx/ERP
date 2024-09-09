package com.orion.erp_back.services

import com.fasterxml.jackson.databind.ObjectMapper
import com.orion.erp_back.utils.toDate
import org.apache.poi.hssf.usermodel.HSSFDataFormat
import org.apache.poi.hssf.usermodel.HSSFWorkbook
import org.apache.poi.ss.usermodel.BorderStyle
import org.apache.poi.ss.usermodel.Workbook
import org.springframework.core.io.ByteArrayResource
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder
import org.springframework.stereotype.Component
import java.io.ByteArrayOutputStream
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.Date
import kotlin.reflect.KProperty1
import kotlin.reflect.full.memberProperties

/**
 * Service for exporting data to Excel format using Apache POI.
 */
@Component
class PoiExportService {

    // Jackson ObjectMapper instance for handling JSON conversions
    private val mapper = Jackson2ObjectMapperBuilder().build<ObjectMapper>()

    // Regex pattern to identify properties with nested fields (e.g., "user.name")
    private val dotRegex = Regex("\\.")

    /**
     * Converts the provided workbook into a downloadable response entity.
     *
     * @param wb The workbook to convert.
     * @param name The name of the file to be downloaded.
     * @return A ResponseEntity containing the workbook as a ByteArrayResource.
     */
    fun toResponseEntity(wb: Workbook, name: String): ResponseEntity<ByteArrayResource> {
        val headers = HttpHeaders().apply {
            add("Content-Disposition", "filename=\"Export-${name}-${Date().time}.xls\"")
        }
        val bos = ByteArrayOutputStream()
        bos.use {
            wb.apply {
                write(bos)
            }
        }
        val resource = ByteArrayResource(bos.toByteArray())
        return ResponseEntity.ok()
            .headers(headers)
            .contentLength(resource.contentLength())
            .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
            .body(resource)
    }

    /**
     * Creates an Excel workbook with the given title, headers, and result set.
     *
     * @param title The title of the Excel sheet.
     * @param headers The collection of headers for the Excel columns.
     * @param result The data to be populated in the Excel sheet.
     * @return The generated Excel workbook.
     */
    fun buildExcelDocument(
        title: String? = "Export",
        headers: Collection<String>,
        result: Collection<Any>
    ) = buildExcelDocument(
        title,
        headers.associateWith { header -> header.replaceFirstChar { c -> c.titlecase() } },
        result
    )

    /**
     * Creates an Excel workbook with the given title, headers map, and result set.
     *
     * @param title The title of the Excel sheet.
     * @param headers A map of field names to header names for the Excel columns.
     * @param result The data to be populated in the Excel sheet.
     * @return The generated Excel workbook.
     */
    fun buildExcelDocument(
        title: String? = "Export",
        headers: Map<String, String>,
        result: Collection<Any>
    ): Workbook = HSSFWorkbook().apply {
        val sheet = createSheet(title).apply {
            defaultColumnWidth = 40
        }
        val headerFont = createFont().apply {
            bold = true
        }
        val headerCellStyle = createCellStyle().apply {
            setFont(headerFont)
            borderBottom = BorderStyle.MEDIUM
        }
        val dateStyle = createCellStyle().apply {
            dataFormat = HSSFDataFormat.getBuiltinFormat("m/d/yy h:mm")
        }

        val createHelper = creationHelper

        // Creating header row
        var rowNo = 0
        var row = sheet.createRow(rowNo++)

        headers.values.withIndex().forEach { (cellNo, header) ->
            row.createCell(cellNo).apply {
                setCellValue(createHelper.createRichTextString(header))
                setCellStyle(headerCellStyle)
            }
        }

        // Populating data rows
        result.forEach { entity ->
            val list = headers.keys.map { key ->
                readDeepInstanceProperty(entity, key)
            }
            row = sheet.createRow(rowNo++)
            list.withIndex().forEach { (cellNo, cell) ->
                row.createCell(cellNo).apply {
                    when (cell) {
                        is LocalDate -> {
                            setCellValue(cell.toDate())
                            setCellStyle(dateStyle)
                        }

                        is LocalDateTime -> {
                            setCellValue(cell.toDate())
                            setCellStyle(dateStyle)
                        }

                        is Number -> setCellValue(cell.toDouble())
                        is String -> setCellValue(cell as String?)
                        is Boolean -> setCellValue((cell as Boolean?)!!)
                        is Collection<*> -> setCellValue(cell.joinToString("; "))
                        else -> setCellValue(mapper.writeValueAsString(cell))
                    }
                }
            }
        }
    }

    /**
     * Reads a nested property value from an instance.
     *
     * @param instance The object instance to read from.
     * @param propertyName The property name (supports dot notation for nested fields).
     * @return The value of the property.
     */
    private fun readDeepInstanceProperty(instance: Any, propertyName: String): Any {
        var property = propertyName
        var obj = instance
        if (dotRegex.containsMatchIn(propertyName)) {
            do {
                val (o, p) = property.split(dotRegex, 2)
                property = p
                obj = readInstanceProperty(obj, o)
            } while (dotRegex.matches(property))
        }
        return readInstanceProperty(obj, property)
    }

    /**
     * Reads a property value from an instance.
     *
     * @param instance The object instance to read from.
     * @param propertyName The property name.
     * @return The value of the property.
     */

    @Suppress("UNCHECKED_CAST")
    private fun readInstanceProperty(instance: Any, propertyName: String): Any = try {
        (instance::class.memberProperties
            .first { it.name == propertyName } as KProperty1<Any, *>).get(instance)!!
    } catch (e: Exception) {
        ""
    }
}