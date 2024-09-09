package com.orion.erp_back.user.dto.serve.response

import com.orion.erp_back.user.constant.UserRole
import com.orion.erp_back.user.entity.User

data class CreateUserResponse(
    val userId: Long,

    val role: UserRole,

    val username: String,

    val email: String,

    val firstName: String,

    val lastName: String,

    val accessToken: String,
) {
    companion object {
        fun of(user: User, accessToken: String): CreateUserResponse {
            return with(user) {
                CreateUserResponse(
                    userId = id,
                    role = role,
                    username = username,
                    email = email,
                    firstName = firstName,
                    lastName = lastName,
                    accessToken = accessToken,
                )
            }
        }
    }
}
