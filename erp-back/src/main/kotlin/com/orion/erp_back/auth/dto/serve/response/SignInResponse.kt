package com.orion.erp_back.auth.dto.serve.response

import com.orion.erp_back.user.constant.UserRole
import com.orion.erp_back.user.entity.User
import io.swagger.v3.oas.annotations.media.Schema

class SignInResponse(
    @field:Schema(
        description = "User Access Token",
        nullable = true
    )
    val accessToken: String?,

    @field:Schema(description = "2FA required or not", nullable = false)
    val twoFactorRequired: Boolean,

    @field:Schema(description = "User Id", nullable = false)
    val userId: Long,

    @field:Schema(description = "User Role", nullable = false, implementation = UserRole::class)
    val role: UserRole,

    @field:Schema(description = "User Name", nullable = false)
    val username: String,

    @field:Schema(description = "User Email", nullable = false, format = "email")
    val email: String,
) {
    companion object {
        fun of(user: User, accessToken: String): SignInResponse {
            return with(user) {
                SignInResponse(
                    accessToken = accessToken,
                    twoFactorRequired = isUsing2FA,

                    userId = id,
                    role = role,
                    username = username,
                    email = email,
                )
            }
        }

        fun ofTwoFactorRequired(user: User, accessToken: String): SignInResponse {
            return with(user) {
                SignInResponse(
                    accessToken = accessToken,
                    twoFactorRequired = isUsing2FA,

                    userId = id,
                    role = role,
                    username = username,
                    email = email,
                )
            }
        }
    }
}