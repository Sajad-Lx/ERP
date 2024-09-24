package com.orion.erp_back.user.dto.serve.response

import com.orion.erp_back.user.constant.UserRole
import com.orion.erp_back.user.entity.User
import io.swagger.v3.oas.annotations.media.Schema

data class UpdateUserResponse(
    @field:Schema(description = "User Id", nullable = false)
    val userId: Long,

    @field:Schema(description = "User Role", nullable = false, implementation = UserRole::class)
    val role: UserRole,

    @field:Schema(description = "User Name", nullable = false)
    val username: String,

    @field:Schema(description = "User Email", nullable = false, format = "email")
    val email: String,

    @field:Schema(description = "First Name", nullable = false)
    val firstName: String,

    @field:Schema(description = "Last Name", nullable = false)
    val lastName: String,
) {
    companion object {
        fun of(user: User): UpdateUserResponse {
            return with(user) {
                UpdateUserResponse(
                    userId = id,
                    role = role,
                    username = username,
                    email = email,
                    firstName = firstName,
                    lastName = lastName,
                )
            }
        }
    }
}