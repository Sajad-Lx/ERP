package com.orion.erp_back.user.dto.serve.request

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

data class CreateUserRequest(
    @field:Schema(description = "User Name", nullable = false)
    @field:NotBlank(message = "field username is blank")
    val username: String,

    @field:Schema(description = "User Email", nullable = false, format = "email")
    @field:NotBlank(message = "field email is blank")
    @field:Email(message = "field email is not email format")
    val email: String,

    @field:Schema(description = "First Name", nullable = false)
    @field:NotBlank(message = "field first name is blank")
    val firstName: String,

    @field:Schema(description = "Last Name", nullable = false)
    @field:NotBlank(message = "field last name is blank")
    val lastName: String,

    @field:Schema(description = "User Password", nullable = false)
    @field:NotBlank(message = "field password is blank")
    @field:Size(
        min = 8,
        max = 20,
        message = "field password is min size 8 and max size 20"
    )
    val password: String
)
