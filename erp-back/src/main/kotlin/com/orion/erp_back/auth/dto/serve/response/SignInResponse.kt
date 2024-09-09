package com.orion.erp_back.auth.dto.serve.response

import com.orion.erp_back.user.constant.UserRole
import com.orion.erp_back.user.entity.User

class SignInResponse(
    val accessToken: String?,
    val twoFactorRequired: Boolean,
    val message: String,

    val userId: Long,
    val role: UserRole,
    val username: String,
    val email: String,
) {
    companion object {
        fun of(user: User, accessToken: String): SignInResponse {
            return with(user) {
                SignInResponse(
                    accessToken = accessToken,
                    twoFactorRequired = isUsing2FA,
                    message = "Sign-in successful",

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
                    message = "2FA code required",

                    userId = id,
                    role = role,
                    username = username,
                    email = email,
                )
            }
        }
    }
}