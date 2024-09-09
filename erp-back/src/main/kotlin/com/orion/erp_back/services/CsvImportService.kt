package com.orion.erp_back.services

import com.opencsv.bean.CsvToBeanBuilder
import org.apache.commons.io.input.BOMInputStream.builder
import org.springframework.stereotype.Component
import org.springframework.web.multipart.MultipartFile

/**
 * Service to import CSV files and convert them to a collection of entities.
 */
@Component
class CsvImportService {

    /**
     * Parses the provided CSV file and converts it to a collection of entities.
     *
     * @param file The CSV file to import, provided as a MultipartFile.
     * @return A collection of entities represented by the data in the CSV file.
     */
    final inline fun <reified T : CsvImportDto<S>, S> import(file: MultipartFile): Collection<S> =
        builder().setInputStream(file.inputStream).get().bufferedReader().use { stream ->
            CsvToBeanBuilder<T>(stream).withType(T::class.java).withIgnoreLeadingWhiteSpace(true)
                .withSeparator(';').build().parse().map { it.toEntity() }
        }
}

/**
 * Interface to be implemented by DTO classes that convert CSV data to entities.
 */
fun interface CsvImportDto<S> {
    /**
     * Converts the DTO to an entity.
     *
     * @return The entity representation of the DTO.
     */
    fun toEntity(): S
}