package com.orion.erp_back.user.service

import com.orion.erp_back.auth.dto.serve.request.SignInRequest
import com.orion.erp_back.user.entity.User
import com.orion.erp_back.user.exception.UserNotFoundException
import com.orion.erp_back.user.exception.UserUnAuthorizedException
import com.orion.erp_back.user.repository.UserRepository
import com.orion.erp_back.user.service.impl.UserServiceImpl
import org.instancio.Instancio
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
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.test.context.ActiveProfiles

@ActiveProfiles("test")
@Tag("unit-test")
@DisplayName("Unit - User Service Test")
@ExtendWith(
    MockitoExtension::class
)
class UserServiceTests {
    @InjectMocks
    private lateinit var userServiceImpl: UserServiceImpl

    @Mock
    private lateinit var userRepository: UserRepository

    @Mock
    private lateinit var bCryptPasswordEncoder: BCryptPasswordEncoder

    private val user: User = Instancio.create(User::class.java)

    @Nested
    @DisplayName("Validate And Return User Entity Test")
    inner class ValidateReturnUserTest {
        @Test
        @DisplayName("Success validate and get user entity")
        fun `should assert User entity when given userId`() {
            Mockito.`when`(userRepository.findOneById(any<Long>())).thenReturn(user)

            val validateUser = userServiceImpl.validateReturnUser(user.id)

            assertNotNull(validateUser)
            assertEquals(user.id, validateUser.id)
            assertEquals(user.email, validateUser.email)
            assertEquals(user.username, validateUser.username)
            assertEquals(user.firstName, validateUser.firstName)
            assertEquals(user.lastName, validateUser.lastName)
            assertEquals(user.role, validateUser.role)
        }

        @Test
        @DisplayName("validate and user entity is not found exception")
        fun `should assert UserNotFoundException when given userId`() {
            Mockito.`when`(userRepository.findOneById(any<Long>())).thenReturn(null)

            Assertions.assertThrows(
                UserNotFoundException::class.java
            ) { userServiceImpl.validateReturnUser(user.id) }
        }
    }

    @Nested
    @DisplayName("Validate and authenticated Return User Entity")
    inner class ValidateAuthReturnUserTest {
        private val signInRequest: SignInRequest = Instancio.create(SignInRequest::class.java)

        @Test
        @DisplayName("Success validate and authenticated get user entity")
        fun `should assert User entity when given SignInRequest`() {
            Mockito.`when`(userRepository.findOneByEmail(any<String>())).thenReturn(user)

            Mockito.`when`(
                user.validatePassword(
                    signInRequest.password, bCryptPasswordEncoder
                )
            ).thenReturn(true)

            val validateAuthUser = userServiceImpl.validateAuthReturnUser(
                signInRequest
            )

            assertNotNull(validateAuthUser)
            assertEquals(user.id, validateAuthUser.id)
            assertEquals(user.email, validateAuthUser.email)
            assertEquals(user.username, validateAuthUser.username)
            assertEquals(user.firstName, validateAuthUser.firstName)
            assertEquals(user.lastName, validateAuthUser.lastName)
            assertEquals(user.role, validateAuthUser.role)
        }

        @Test
        @DisplayName("validate and authenticated user is not found exception")
        fun `should assert UserNotFoundException when given SignInRequest`() {
            Mockito.`when`(userRepository.findOneByEmail(any<String>())).thenReturn(null)

            Assertions.assertThrows(
                UserNotFoundException::class.java
            ) { userServiceImpl.validateAuthReturnUser(signInRequest) }
        }

        @Test
        @DisplayName("validate and authenticated user is unauthorized exception")
        fun `should assert UserUnAuthorizedException when given SignInRequest`() {
            Mockito.`when`(userRepository.findOneByEmail(any<String>())).thenReturn(user)

            Mockito.`when`(
                user.validatePassword(
                    signInRequest.password, bCryptPasswordEncoder
                )
            ).thenReturn(false)

            Assertions.assertThrows(
                UserUnAuthorizedException::class.java
            ) { userServiceImpl.validateAuthReturnUser(signInRequest) }
        }
    }
}
