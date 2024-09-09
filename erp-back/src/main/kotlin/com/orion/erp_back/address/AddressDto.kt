package com.orion.erp_back.address

import java.time.LocalDateTime

data class AddressDto(
    var id: Long?,
    var name: String?,
    var street: String?,
    var zip: String?,
    var city: String?,
    var email: String?,
    var tel: String?,
    var enabled: Boolean?,
    var lastModified: LocalDateTime?,
    var options: Map<String, Any>?,
    var things: Collection<String>?
)
