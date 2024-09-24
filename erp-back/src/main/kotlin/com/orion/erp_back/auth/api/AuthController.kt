package com.orion.erp_back.auth.api

import com.orion.erp_back.auth.dto.serve.request.SignInRequest
import com.orion.erp_back.auth.dto.serve.request.TwoFaVerifyRequest
import com.orion.erp_back.auth.dto.serve.response.RefreshAccessTokenResponse
import com.orion.erp_back.auth.dto.serve.response.SignInResponse
import com.orion.erp_back.auth.service.AuthService
import com.orion.erp_back.common.response.ErrorResponse
import com.orion.erp_back.security.SecurityUserItem
import com.orion.erp_back.security.annotation.CurrentUser
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@Tag(name = "Auth", description = "Auth API")
@RestController
@RequestMapping("/api/v1/auth")
class AuthController(
    private val authService: AuthService
) {
    @Operation(operationId = "signIn", summary = "Sign In", description = "User Sign In API")
    @ApiResponses(
        value = [ApiResponse(
            responseCode = "200",
            description = "OK",
            content = arrayOf(Content(schema = Schema(implementation = SignInResponse::class)))
        ), ApiResponse(
            responseCode = "400",
            description = "Request Body Valid Error",
            content = arrayOf(Content(schema = Schema(implementation = ErrorResponse::class)))
        ), ApiResponse(
            responseCode = "401",
            description = "User UnAuthorized userId = {userId} or email = {email}",
            content = arrayOf(Content(schema = Schema(implementation = ErrorResponse::class)))
        ), ApiResponse(
            responseCode = "404",
            description = "User Not Found userId = {userId} or email = {email}",
            content = arrayOf(Content(schema = Schema(implementation = ErrorResponse::class)))
        )]
    )
    @PostMapping("/signIn")
    fun signIn(
        @RequestBody @Valid signInRequest: SignInRequest
    ): ResponseEntity<SignInResponse> = ResponseEntity.ok(authService.signIn(signInRequest))

    @Operation(
        operationId = "verify-2fa",
        summary = "Verify 2FA",
        description = "User Verify using 2FA code"
    )
    @ApiResponses(
        value = [ApiResponse(
            responseCode = "200",
            description = "OK",
            content = arrayOf(Content(schema = Schema(implementation = SignInResponse::class)))
        ), ApiResponse(
            responseCode = "400",
            description = "Request Body Valid Error",
            content = arrayOf(Content(schema = Schema(implementation = ErrorResponse::class)))
        ), ApiResponse(
            responseCode = "401",
            description = "User UnAuthorized userId = {userId} or email = {email}",
            content = arrayOf(Content(schema = Schema(implementation = ErrorResponse::class)))
        ), ApiResponse(
            responseCode = "404",
            description = "User Not Found userId = {userId} or email = {email}",
            content = arrayOf(Content(schema = Schema(implementation = ErrorResponse::class)))
        )]
    )
    @PostMapping("/verify-2fa")
    fun verify2fa(
        @RequestBody @Valid twoFaVerifyRequest: TwoFaVerifyRequest
    ): ResponseEntity<SignInResponse> = ResponseEntity.ok(authService.verify2Fa(twoFaVerifyRequest))

    @Operation(operationId = "signOut", summary = "Sign Out", description = "User Sign Out API")
    @ApiResponses(
        value = [ApiResponse(responseCode = "200", description = "OK"), ApiResponse(
            responseCode = "401",
            description = "Full authentication is required to access this resource",
            content = arrayOf(Content(schema = Schema(implementation = ErrorResponse::class)))
        )]
    )
    @PostMapping("/signOut")
    fun signOut(
        @CurrentUser securityUserItem: SecurityUserItem
    ): ResponseEntity<Void> {
        authService.signOut(securityUserItem.userId)

        return ResponseEntity.ok().build()
    }

    @Operation(
        operationId = "refreshAccessToken",
        summary = "Refresh AccessToken",
        description = "User Refresh AccessToken API"
    )
    @ApiResponses(
        value = [ApiResponse(
            responseCode = "201",
            description = "Create AccessToken",
            content = arrayOf(Content(schema = Schema(implementation = RefreshAccessTokenResponse::class)))
        ), ApiResponse(
            responseCode = "401", description = """
                1. Full authentication is required to access this resource
                2. Refresh Token Not Found userId = {userId}
                3. Refresh Token is Expired
            """, content = arrayOf(Content(schema = Schema(implementation = ErrorResponse::class)))
        )]
    )
    @PostMapping("/refresh")
    fun refreshAccessToken(
        @CurrentUser securityUserItem: SecurityUserItem
    ): ResponseEntity<RefreshAccessTokenResponse> = ResponseEntity.status(HttpStatus.CREATED).body(
        authService.refreshAccessToken(
            securityUserItem
        )
    )
}
