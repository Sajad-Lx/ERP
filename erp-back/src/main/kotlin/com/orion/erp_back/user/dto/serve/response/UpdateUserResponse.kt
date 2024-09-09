package com.orion.erp_back.user.dto.serve.response

import com.orion.erp_back.user.constant.UserRole
import com.orion.erp_back.user.entity.User

data class UpdateUserResponse(
    val userId: Long,

    val role: UserRole,

    val username: String,

    val email: String,

    val firstName: String,

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