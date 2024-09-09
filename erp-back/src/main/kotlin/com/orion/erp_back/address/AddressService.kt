package com.orion.erp_back.address

import com.orion.erp_back.services.CsvImportService
import com.orion.erp_back.services.PoiExportService
import org.springframework.core.io.ByteArrayResource
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Component
import org.springframework.web.multipart.MultipartFile
import java.util.Optional

@Component
class AddressService(
    private val addressRepository: AddressRepository,
    private val csvImportService: CsvImportService,
    private val poiExportService: PoiExportService,
) {
    fun list(): Collection<Address> = addressRepository.findAll()

    fun findById(id: Long): Optional<Address> = addressRepository.findById(id)

    fun save(dto: AddressDto): Address = addressRepository.save(Address.fromDTO(dto))

    fun delete(id: Long) = addressRepository.deleteById(id)

    fun import(file: MultipartFile): Collection<Address> =
        csvImportService.import<AddressImportDto, Address>(file)
            .also { addressRepository.saveAll(it) }

    fun export(): ResponseEntity<ByteArrayResource> {
        val result = addressRepository.findAll().map { it.toDTO() }
        val wb = poiExportService.buildExcelDocument(
            "Export Address List", listOf(
                "id",
                "name",
                "street",
                "zip",
                "city",
                "email",
                "tel",
                "enabled",
                "things",
                "options",
                "lastModified"
            ), result
        )
        return poiExportService.toResponseEntity(wb, "Address-List")
    }
}