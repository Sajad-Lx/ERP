package com.orion.erp_back.auth.service

import com.orion.erp_back.auth.dto.serve.request.SignInRequest
import com.orion.erp_back.auth.dto.serve.request.TwoFaVerifyRequest
import com.orion.erp_back.security.SecurityUserItem
import com.orion.erp_back.security.component.provider.TokenProvider
import com.orion.erp_back.security.exception.RefreshTokenNotFoundException
import com.orion.erp_back.user.entity.User
import com.orion.erp_back.user.exception.UserNotFoundException
import com.orion.erp_back.user.exception.UserUnAuthorizedException
import com.orion.erp_back.user.service.impl.UserServiceImpl
import io.jsonwebtoken.Claims
import io.jsonwebtoken.ExpiredJwtException
import org.instancio.Instancio
import org.jboss.aerogear.security.otp.Totp
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.any
import org.springframework.test.context.ActiveProfiles

@ActiveProfiles("test")
@Tag("unit-test")
@DisplayName("Unit - Auth Service Test")
@ExtendWith(
    MockitoExtension::class
)
class AuthServiceTests {
    private val user: User = Instancio.create(User::class.java).copy(isUsing2FA = false)

    private val userWith2Fa: User = Instancio.create(User::class.java).copy(isUsing2FA = true)

    private val defaultAccessToken =
        "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c\n" +  //
                ""

    @Mock
    private lateinit var tokenProvider: TokenProvider

    @Mock
    private lateinit var userServiceImpl: UserServiceImpl

    @InjectMocks
    private lateinit var authService: AuthService

    @Nested
    @DisplayName("Sign In Test")
    inner class SignInTest {
        private val signInRequest: SignInRequest = Instancio.create(SignInRequest::class.java)

        private val twoFaRequest: TwoFaVerifyRequest =
            Instancio.create(TwoFaVerifyRequest::class.java)
                .copy(twoFaCode = Totp(userWith2Fa.secret).now())

        @Test
        @DisplayName("Success sign in")
        fun `should assert SignInResponse when given SignInRequest`() {
            Mockito.`when`(userServiceImpl.validateAuthReturnUser(any<SignInRequest>()))
                .thenReturn(user)

            Mockito.`when`(tokenProvider.createFullTokens(any<User>()))
                .thenReturn(defaultAccessToken)

            val signInResponse = authService.signIn(signInRequest)

            assertNotNull(signInResponse)
            assertEquals(user.email, signInResponse.email)
            assertEquals(user.username, signInResponse.username)
            assertEquals(defaultAccessToken, signInResponse.accessToken)
        }

        @Test
        @DisplayName("Verify 2FA code")
        fun `should assert verification code when given TwoFaVerifyRequest`() {
            Mockito.`when`(userServiceImpl.validate2Fa(any<TwoFaVerifyRequest>()))
                .thenReturn(userWith2Fa)

            Mockito.`when`(tokenProvider.createFullTokens(any<User>()))
                .thenReturn(defaultAccessToken)

            val signInResponse = authService.verify2Fa(twoFaRequest)

            assertNotNull(signInResponse)
            assertEquals(userWith2Fa.email, signInResponse.email)
            assertEquals(userWith2Fa.username, signInResponse.username)
            assertEquals(defaultAccessToken, signInResponse.accessToken)
        }

        @Test
        @DisplayName("User not found")
        fun `should assert UserNotFoundException when given SignInRequest`() {
            Mockito.`when`(userServiceImpl.validateAuthReturnUser(any<SignInRequest>()))
                .thenThrow(UserNotFoundException(user.id))

            Assertions.assertThrows(
                UserNotFoundException::class.java
            ) { authService.signIn(signInRequest) }
        }

        @Test
        @DisplayName("User unauthorized")
        fun `should assert UserUnAuthorizedException when GivenSignInRequest`() {
            Mockito.`when`(userServiceImpl.validateAuthReturnUser(any<SignInRequest>()))
                .thenThrow(UserUnAuthorizedException(user.email))

            Assertions.assertThrows(
                UserUnAuthorizedException::class.java
            ) { authService.signIn(signInRequest) }
        }
    }

    @Nested
    @DisplayName("Sign Out Test")
    inner class SignOutTest {
        @Test
        @DisplayName("Success sign out")
        fun `should verify call deleteRefreshToken when given UserId`() {
            authService.signOut(user.id)

            Mockito.verify(tokenProvider, Mockito.times(1)).deleteRefreshToken(any<Long>())
        }
    }

    @Nested
    @DisplayName("Refresh Access Token Test")
    inner class RefreshTokenTest {
        private val securityUserItem: SecurityUserItem = Instancio.create(
            SecurityUserItem::class.java
        )

        @Test
        @DisplayName("Success refresh access token")
        fun `should assert refreshAccessTokenResponse when given SecurityUserItem`() {
            Mockito.`when`(tokenProvider.refreshAccessToken(any<SecurityUserItem>()))
                .thenReturn(defaultAccessToken)

            val refreshAccessTokenResponse = authService.refreshAccessToken(
                securityUserItem
            )

            assertNotNull(refreshAccessTokenResponse)
            assertEquals(
                defaultAccessToken,
                refreshAccessTokenResponse.accessToken
            )
        }

        @Test
        @DisplayName("Refresh token is expired")
        fun `should assert ExpiredJwtException when given SecurityUserItem`() {
            val claims = Instancio.create(Claims::class.java)

            Mockito.`when`(
                tokenProvider.refreshAccessToken(any<SecurityUserItem>())
            )
                .thenThrow(
                    ExpiredJwtException(
                        null,
                        claims,
                        "JWT expired at ?. Current time: ?, a difference of ? milliseconds.  Allowed clock skew: ? milliseconds."
                    )
                )

            Assertions.assertThrows(
                ExpiredJwtException::class.java
            ) { authService.refreshAccessToken(securityUserItem) }
        }

        @Test
        @DisplayName("Refresh token is not found")
        fun `should assert RefreshTokenNotFoundException when given SecurityUserItem`() {
            Mockito.`when`(tokenProvider.refreshAccessToken(any<SecurityUserItem>()))
                .thenThrow(RefreshTokenNotFoundException(user.id))

            Assertions.assertThrows(
                RefreshTokenNotFoundException::class.java
            ) { authService.refreshAccessToken(securityUserItem) }
        }
    }
}