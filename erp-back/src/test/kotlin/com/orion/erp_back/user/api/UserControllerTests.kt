package com.orion.erp_back.user.api

import com.orion.erp_back.security.SecurityUserItem
import com.orion.erp_back.user.dto.serve.request.CreateUserRequest
import com.orion.erp_back.user.dto.serve.request.UpdateUserRequest
import com.orion.erp_back.user.dto.serve.response.CreateUserResponse
import com.orion.erp_back.user.dto.serve.response.GetUserResponse
import com.orion.erp_back.user.dto.serve.response.UpdateMeResponse
import com.orion.erp_back.user.dto.serve.response.UpdateUserResponse
import com.orion.erp_back.user.entity.User
import com.orion.erp_back.user.service.impl.ChangeUserServiceImpl
import com.orion.erp_back.user.service.impl.GetUserServiceImpl
import org.assertj.core.api.Assertions.assertThat
import org.instancio.Instancio
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
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.http.HttpStatus
import org.springframework.test.context.ActiveProfiles

@ActiveProfiles("test")
@Tag("unit-test")
@DisplayName("Unit - User Controller Test")
@ExtendWith(
    MockitoExtension::class
)
class UserControllerTests {
    private val user: User = Instancio.create(User::class.java)

    private val defaultAccessToken =
        "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c\n" +  //
                ""
    private val defaultPageable = Pageable.ofSize(1)

    @InjectMocks
    private lateinit var userController: UserController

    @Mock
    private lateinit var getUserServiceImpl: GetUserServiceImpl

    @Mock
    private lateinit var changeUserServiceImpl: ChangeUserServiceImpl

    @Test
    @DisplayName("Get user by id")
    fun `should assert GetUserResponse when given userId`() {
        Mockito.`when`(getUserServiceImpl.getUserById(any<Long>()))
            .thenReturn(GetUserResponse.of(user))

        val response = userController.getUserById(
            user.id
        )

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
    }

    @Test
    @DisplayName("Get user list")
    fun `should assert page of GetUserResponse when given Default Pageable`() {
        Mockito.`when`(getUserServiceImpl.getUserList(any<Pageable>()))
            .thenReturn(PageImpl(listOf(GetUserResponse.of(user)), defaultPageable, 1))

        val response = userController.getUserList(
            defaultPageable
        )

        assertNotNull(response)
        assertNotNull(response.body)
        assertEquals(HttpStatus.OK, response.statusCode)

        val body = requireNotNull(response.body) {
            "Response body must not be null"
        }

        assertThat(body).isNotEmpty()
        assertEquals(user.id, body.content[0].userId)
        assertEquals(user.email, body.content[0].email)
        assertEquals(user.username, body.content[0].username)
        assertEquals(user.role, body.content[0].role)
    }

    @Test
    @DisplayName("Create user")
    fun `should assert CreateUserResponse when given CreateUserRequest`() {
        val createUserRequest = Instancio.create(
            CreateUserRequest::class.java
        )

        Mockito.`when`(changeUserServiceImpl.createUser(any<CreateUserRequest>()))
            .thenReturn(CreateUserResponse.of(user, defaultAccessToken))

        val response = userController.createUser(
            createUserRequest
        )

        assertNotNull(response)
        assertNotNull(response.body)
        assertEquals(HttpStatus.CREATED, response.statusCode)

        val body = requireNotNull(response.body) {
            "Response body must not be null"
        }

        assertEquals(user.email, body.email)
        assertEquals(user.username, body.username)
        assertEquals(defaultAccessToken, body.accessToken)
    }

    @Test
    @DisplayName("Update user")
    fun `should assert UpdateUserResponse when given userId and UpdateUserRequest`() {
        val updateUserRequest = Instancio.create(
            UpdateUserRequest::class.java
        )

        Mockito.`when`(changeUserServiceImpl.updateUser(any<Long>(), any<UpdateUserRequest>()))
            .thenReturn(UpdateUserResponse.of(user))

        val response = userController.updateUser(
            updateUserRequest, user.id
        )

        assertNotNull(response)
        assertNotNull(response.body)
        assertEquals(HttpStatus.OK, response.statusCode)

        val body = requireNotNull(response.body) {
            "Response body must not be null"
        }

        assertEquals(user.email, body.email)
        assertEquals(user.username, body.username)
        assertEquals(user.role, body.role)
    }

    @Test
    @DisplayName("Update me")
    fun `should assert UpdateMeResponse when given SecurityUserItem and UpdateUserRequest`() {
        val updateUserRequest = Instancio.create(
            UpdateUserRequest::class.java
        )
        val securityUserItem = Instancio.create(
            SecurityUserItem::class.java
        )

        Mockito.`when`(changeUserServiceImpl.updateMe(any<Long>(), any<UpdateUserRequest>()))
            .thenReturn(UpdateMeResponse.of(user, defaultAccessToken))

        val response = userController.updateMe(
            updateUserRequest, securityUserItem
        )

        assertNotNull(response)
        assertNotNull(response.body)
        assertEquals(HttpStatus.OK, response.statusCode)

        val body = requireNotNull(response.body) {
            "Response body must not be null"
        }

        assertEquals(user.email, body.email)
        assertEquals(user.username, body.username)
        assertEquals(user.role, body.role)
        assertEquals(defaultAccessToken, body.accessToken)
    }

    @Test
    @DisplayName("Delete user")
    fun `should verify call DeleteUser method when given userId`() {
        val response = userController.deleteUser(user.id)

        assertNotNull(response)
        assertNull(response.body)
        assertEquals(HttpStatus.NO_CONTENT, response.statusCode)

        Mockito.verify(changeUserServiceImpl, Mockito.times(1)).deleteUser(any<Long>())
    }
}
