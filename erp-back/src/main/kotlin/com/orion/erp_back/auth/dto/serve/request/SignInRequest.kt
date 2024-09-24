package com.orion.erp_back.auth.dto.serve.request

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size
import io.swagger.v3.oas.annotations.media.Schema

data class SignInRequest(
    @field:Schema(description = "User Email", nullable = false, format = "email")
    @field:NotBlank(message = "Email cannot be blank")
    @field:Email(message = "Invalid email format")
    val email: String,

    @field:Schema(description = "User Password", nullable = false)
    @field:NotBlank(message = "Password cannot be blank")
    @field:Size(
        min = 8,
        max = 20,
        message = "field password is min size 8 and max size 20"
    )
    val password: String,
)