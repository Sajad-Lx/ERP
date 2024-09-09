package com.orion.erp_back.auth.service

import com.orion.erp_back.auth.dto.serve.request.SignInRequest
import com.orion.erp_back.auth.dto.serve.request.TwoFaVerifyRequest
import com.orion.erp_back.auth.dto.serve.response.RefreshAccessTokenResponse
import com.orion.erp_back.auth.dto.serve.response.SignInResponse
import com.orion.erp_back.security.SecurityUserItem
import com.orion.erp_back.security.component.provider.TokenProvider
import com.orion.erp_back.user.entity.User
import com.orion.erp_back.user.service.UserService
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service

@Service
class AuthService(
    private val userService: UserService, private val tokenProvider: TokenProvider
) {
    fun signIn(signInRequest: SignInRequest): SignInResponse {
        val user: User = userService.validateAuthReturnUser(signInRequest)

        if (user.isUsing2FA) return user.let {
            SignInResponse.ofTwoFactorRequired(it, tokenProvider.createAccessToken(it))
        }

        return user.let {
            SignInResponse.of(it, tokenProvider.createFullTokens(it))
        }
    }

    fun verify2Fa(twoFaVerifyRequest: TwoFaVerifyRequest): SignInResponse {
        val user: User = userService.validate2Fa(twoFaVerifyRequest)

        return user.let {
            SignInResponse.of(user, tokenProvider.createFullTokens(it))
        }
    }

    fun signOut(userId: Long) {
        tokenProvider.deleteRefreshToken(userId)
        SecurityContextHolder.clearContext()
    }

    fun refreshAccessToken(
        securityUserItem: SecurityUserItem
    ): RefreshAccessTokenResponse = RefreshAccessTokenResponse.of(
        tokenProvider.refreshAccessToken(securityUserItem)
    )
}