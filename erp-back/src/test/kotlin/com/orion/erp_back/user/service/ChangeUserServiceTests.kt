package com.orion.erp_back.user.service

import com.orion.erp_back.security.component.provider.TokenProvider
import com.orion.erp_back.user.dto.serve.request.CreateUserRequest
import com.orion.erp_back.user.dto.serve.request.UpdateUserRequest
import com.orion.erp_back.user.entity.User
import com.orion.erp_back.user.exception.AlreadyUserExistException
import com.orion.erp_back.user.exception.UserNotFoundException
import com.orion.erp_back.user.repository.UserRepository
import com.orion.erp_back.user.service.impl.ChangeUserServiceImpl
import com.orion.erp_back.user.service.impl.GetUserServiceImpl
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
@DisplayName("Unit - Post / Put / Delete / Patch User Service Test")
@ExtendWith(
    MockitoExtension::class
)
class ChangeUserServiceTests {
    @Mock
    private lateinit var userRepository: UserRepository

    @Mock
    private lateinit var bCryptPasswordEncoder: BCryptPasswordEncoder

    @Mock
    private lateinit var tokenProvider: TokenProvider

    @Mock
    private lateinit var getUserServiceImpl: GetUserServiceImpl

    @Mock
    private lateinit var userServiceImpl: UserServiceImpl

    @InjectMocks
    private lateinit var changeUserServiceImpl: ChangeUserServiceImpl

    private val user: User = Instancio.create(User::class.java)

    private val defaultUserEncodePassword =
        "$2a$10\$T44NRNpbxkQ9qHbCtqQZ7O3gYfipzC0cHvOIJ/aV4PTlvJjtDl7x2\n" +  //
                ""

    private val defaultAccessToken =
        "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c\n" +  //
                ""

    @Nested
    @DisplayName("Delete User Test")
    inner class DeleteTest {
        @Test
        @DisplayName("Success delete user")
        fun `should verify call DeleteRefreshToken and deleteById methods when given userId`() {
            changeUserServiceImpl.deleteUser(user.id)

            Mockito.verify(tokenProvider, Mockito.times(1)).deleteRefreshToken(any<Long>())
            Mockito.verify(userRepository, Mockito.times(1)).deleteById(any<Long>())
        }
    }

    @Nested
    @DisplayName("Update User Test")
    inner class UpdateTest {
        private val updateUserRequest: UpdateUserRequest = Instancio.create(
            UpdateUserRequest::class.java
        )

        @Test
        @DisplayName("Success update user")
        fun `should assert UpdateUserResponse when given userId and UpdateUserRequest`() {
            Mockito.`when`(userServiceImpl.validateReturnUser(any<Long>())).thenReturn(user)

            val updateUserResponse = changeUserServiceImpl.updateUser(
                user.id, updateUserRequest
            )

            assertNotNull(updateUserResponse)
            assertEquals(user.username, updateUserResponse.username)
            assertEquals(user.role, updateUserResponse.role)
        }

        @Test
        @DisplayName("Not found user")
        fun `should assert UserNotFoundException when given userId and UpdateUserRequest`() {
            Mockito.`when`(userServiceImpl.validateReturnUser(any<Long>()))
                .thenThrow(UserNotFoundException(user.id))

            Assertions.assertThrows(
                UserNotFoundException::class.java
            ) { changeUserServiceImpl.updateUser(user.id, updateUserRequest) }
        }
    }

    @Nested
    @DisplayName("Create User Test")
    inner class RegisterTest {
        private val createUserRequest: CreateUserRequest = Instancio.create(
            CreateUserRequest::class.java
        )

        @Test
        @DisplayName("Success create user")
        fun `should assert CreateUserResponse when given CreateUserRequest`() {
            Mockito.`when`(bCryptPasswordEncoder.encode(any<String>()))
                .thenReturn(defaultUserEncodePassword)

            Mockito.`when`(userRepository.save(any<User>())).thenReturn(user)

            Mockito.`when`(tokenProvider.createFullTokens(any<User>()))
                .thenReturn(defaultAccessToken)

            val createUserResponse = changeUserServiceImpl.createUser(
                createUserRequest
            )

            assertNotNull(createUserResponse)
            assertEquals(user.email, createUserResponse.email)
            assertEquals(user.username, createUserResponse.username)
        }

        @Test
        @DisplayName("Already user exist")
        fun `should assert AlreadyUserExistException when given CreateUserRequest`() {
            Mockito.`when`(userRepository.existsByEmail(any<String>())).thenReturn(true)

            Assertions.assertThrows(
                AlreadyUserExistException::class.java
            ) { changeUserServiceImpl.createUser(createUserRequest) }
        }
    }
}
