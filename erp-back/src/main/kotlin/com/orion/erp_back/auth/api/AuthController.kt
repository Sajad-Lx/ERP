package com.orion.erp_back.auth.api

import com.orion.erp_back.auth.dto.serve.request.SignInRequest
import com.orion.erp_back.auth.dto.serve.request.TwoFaVerifyRequest
import com.orion.erp_back.auth.dto.serve.response.RefreshAccessTokenResponse
import com.orion.erp_back.auth.dto.serve.response.SignInResponse
import com.orion.erp_back.auth.service.AuthService
import com.orion.erp_back.security.SecurityUserItem
import com.orion.erp_back.security.annotation.CurrentUser
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/auth")
class AuthController(
    private val authService: AuthService
) {
    @PostMapping("/signIn")
    fun signIn(
        @RequestBody @Valid signInRequest: SignInRequest
    ): ResponseEntity<SignInResponse> = ResponseEntity.ok(authService.signIn(signInRequest))

    @PostMapping("/verify-2fa")
    fun verify2fa(
        @RequestBody @Valid twoFaVerifyRequest: TwoFaVerifyRequest
    ): ResponseEntity<SignInResponse> = ResponseEntity.ok(authService.verify2Fa(twoFaVerifyRequest))

    @PostMapping("/signOut")
    fun signOut(
        @CurrentUser securityUserItem: SecurityUserItem
    ): ResponseEntity<Void> {
        authService.signOut(securityUserItem.userId)

        return ResponseEntity.ok().build()
    }

    @PostMapping("/refresh")
    fun refreshAccessToken(
        @CurrentUser securityUserItem: SecurityUserItem
    ): ResponseEntity<RefreshAccessTokenResponse> = ResponseEntity.status(HttpStatus.CREATED).body(
            authService.refreshAccessToken(
                securityUserItem
            )
        )
}
