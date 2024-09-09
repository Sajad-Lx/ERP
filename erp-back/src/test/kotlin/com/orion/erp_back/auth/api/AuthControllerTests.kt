package com.orion.erp_back.auth.api

import com.orion.erp_back.auth.dto.serve.request.SignInRequest
import com.orion.erp_back.auth.dto.serve.request.TwoFaVerifyRequest
import com.orion.erp_back.auth.dto.serve.response.RefreshAccessTokenResponse
import com.orion.erp_back.auth.dto.serve.response.SignInResponse
import com.orion.erp_back.auth.service.AuthService
import com.orion.erp_back.security.SecurityUserItem
import com.orion.erp_back.user.entity.User
import org.instancio.Instancio
import org.jboss.aerogear.security.otp.Totp
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.any
import org.springframework.http.HttpStatus
import org.springframework.test.context.ActiveProfiles

@ActiveProfiles("test")
@Tag("unit-test")
@DisplayName("Unit - Auth Controller Test")
@ExtendWith(
    MockitoExtension::class
)
class AuthControllerTests {
    @InjectMocks
    private lateinit var authController: AuthController

    @Mock
    private lateinit var authService: AuthService

    private val user: User = Instancio.create(User::class.java)

    private val userWith2Fa: User = Instancio.create(User::class.java).copy(isUsing2FA = true)

    private val defaultAccessToken =
        "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c\n" +  //
                ""

    @Test
    @DisplayName("Sign in")
    fun `should assert SignIn Response when given SignInRequest`() {
        val signInRequest = Instancio.create(SignInRequest::class.java)

        Mockito.`when`(authService.signIn(any<SignInRequest>()))
            .thenReturn(SignInResponse.of(user, defaultAccessToken))

        val response = authController.signIn(signInRequest)

        assertNotNull(response)
        assertNotNull(response.body)
        assertEquals(HttpStatus.OK, response.statusCode)

        val body = requireNotNull(response.body) {
            "Response body must not be null"
        }

        assertEquals(user.id, body.userId)
        assertEquals(user.email, body.email)
        assertEquals(user.username, body.username)
        assertEquals(user.role, body.role)
        assertEquals(defaultAccessToken, body.accessToken)
    }

    @Test
    @DisplayName("Sign in with response 2FA required")
    fun `should expect 2FA required response when user has 2FA enabled`() {
        val signInRequest = Instancio.create(SignInRequest::class.java)

        Mockito.`when`(authService.signIn(any<SignInRequest>()))
            .thenReturn(SignInResponse.ofTwoFactorRequired(userWith2Fa, defaultAccessToken))

        val response = authController.signIn(signInRequest)

        assertNotNull(response)
        assertNotNull(response.body)
        assertEquals(HttpStatus.OK, response.statusCode)

        val body = requireNotNull(response.body) {
            "Response body must not be null"
        }

        assertEquals(userWith2Fa.id, body.userId)
        assertEquals(userWith2Fa.isUsing2FA, body.twoFactorRequired)
        assertEquals(defaultAccessToken, body.accessToken)
    }

    @Test
    @DisplayName("Sign in with 2FA code")
    fun `should assert 2FA verify response when given TwoFaVerifyRequest`() {
        val twoFaRequest = Instancio.create(TwoFaVerifyRequest::class.java).copy(
            userId = userWith2Fa.id, twoFaCode = Totp(userWith2Fa.secret).now()
        )

        Mockito.`when`(authService.verify2Fa(any<TwoFaVerifyRequest>()))
            .thenReturn(SignInResponse.of(userWith2Fa, defaultAccessToken))

        val response = authController.verify2fa(twoFaRequest)

        assertNotNull(response)
        assertNotNull(response.body)
        assertEquals(HttpStatus.OK, response.statusCode)

        val body = requireNotNull(response.body) {
            "Response body must not be null"
        }

        assertEquals(userWith2Fa.id, body.userId)
        assertEquals(userWith2Fa.email, body.email)
        assertEquals(userWith2Fa.role, body.role)
        assertEquals(userWith2Fa.isUsing2FA, body.twoFactorRequired)
        assertEquals(defaultAccessToken, body.accessToken)
    }

    @Test
    @DisplayName("Sign out")
    fun `should assert SignOut Void Response when iven SecurityUserItem`() {
        val securityUserItem = Instancio.create(
            SecurityUserItem::class.java
        )

        val response = authController.signOut(securityUserItem)

        assertNotNull(response)
        assertNull(response.body)
        assertEquals(HttpStatus.OK, response.statusCode)
    }

    @Test
    @DisplayName("Refresh access token")
    fun `should assert RefreshAccessTokenResponse when given SecurityUserItem`() {
        val securityUserItem = Instancio.create(
            SecurityUserItem::class.java
        )

        Mockito.`when`(authService.refreshAccessToken(any<SecurityUserItem>()))
            .thenReturn(RefreshAccessTokenResponse.of(defaultAccessToken))

        val response = authController.refreshAccessToken(
            securityUserItem
        )

        assertNotNull(response)
        assertNotNull(response.body)
        assertEquals(HttpStatus.CREATED, response.statusCode)

        val body = requireNotNull(response.body) {
            "Response body must not be null"
        }

        assertEquals(defaultAccessToken, body.accessToken)
    }
}