package com.orion.erp_back.user.dto.serve.request

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

data class CreateUserRequest(
    @field:NotBlank(message = "field username is blank")
    val username: String,

    @field:NotBlank(message = "field first name is blank")
    val firstName: String,

    @field:NotBlank(message = "field last name is blank")
    val lastName: String,

    @field:NotBlank(message = "field email is blank")
    @field:Email(message = "field email is not email format")
    val email: String,

    @field:NotBlank(message = "field password is blank")
    @field:Size(
        min = 8,
        max = 20,
        message = "field password is min size 8 and max size 20"
    )
    val password: String
)
