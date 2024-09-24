package com.orion.erp_back.user.dto.serve.request

import com.orion.erp_back.common.annotation.ValidEnum
import com.orion.erp_back.user.constant.UserRole
import jakarta.validation.constraints.NotBlank
import io.swagger.v3.oas.annotations.media.Schema

data class UpdateUserRequest(
    @field:Schema(description = "User Name", nullable = false)
    @field:NotBlank(message = "field username is blank")
    val username: String,

    @field:Schema(description = "First Name", nullable = false)
    @field:NotBlank(message = "field first name is blank")
    val firstName: String,

    @field:Schema(description = "Last Name", nullable = false)
    @field:NotBlank(message = "field last name is blank")
    val lastName: String,

    @field:Schema(description = "User Role", nullable = false, implementation = UserRole::class)
    @field:ValidEnum(enumClass = UserRole::class, message = "field role is invalid")
    val role: UserRole
)