package com.orion.erp_back.auth.dto.serve.request

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Pattern
import jakarta.validation.constraints.Size

data class TwoFaVerifyRequest(

//    @field:NotBlank(message = "Email cannot be blank")
//    @field:Email(message = "Invalid email format")
//    val email: String,

    @field:Schema(description = "User Id", nullable = false)
    @field:NotNull(message = "UserId cannot be blank")
    val userId: Long,

    @field:Schema(description = "2FA code", nullable = false)
    @field:NotBlank(message = "2FA code cannot be blank")
    @field:Size(
        min = 6,
        max = 6,
        message = "2FA code must be 6 digits"
    )
    @field:Pattern(
        regexp = "\\d{6}",
        message = "2FA code must contain only digits"
    )
    val twoFaCode: String,

//    @field:NotBlank(message = "Token cannot be blank")
//    val authToken: String
)
