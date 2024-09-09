package com.orion.erp_back.user.dto.serve.request

import com.orion.erp_back.common.annotation.ValidEnum
import com.orion.erp_back.user.constant.UserRole
import jakarta.validation.constraints.NotBlank

data class UpdateUserRequest(
    @field:NotBlank(message = "field username is blank")
    val username: String,

    @field:NotBlank(message = "field first name is blank")
    val firstName: String,

    @field:NotBlank(message = "field last name is blank")
    val lastName: String,

    @field:ValidEnum(enumClass = UserRole::class, message = "field role is invalid")
    val role: UserRole
)