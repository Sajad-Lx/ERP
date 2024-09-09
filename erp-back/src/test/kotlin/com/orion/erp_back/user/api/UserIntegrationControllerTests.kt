package com.orion.erp_back.user.api

import com.orion.erp_back.common.security.SecurityItem
import com.orion.erp_back.common.security.WithMockCustomUser
import com.orion.erp_back.user.dto.serve.request.CreateUserRequest
import com.orion.erp_back.user.dto.serve.request.UpdateUserRequest
import com.orion.erp_back.user.dto.serve.response.CreateUserResponse
import com.orion.erp_back.user.dto.serve.response.GetUserResponse
import com.orion.erp_back.user.dto.serve.response.UpdateMeResponse
import com.orion.erp_back.user.dto.serve.response.UpdateUserResponse
import com.orion.erp_back.user.entity.User
import com.orion.erp_back.user.exception.AlreadyUserExistException
import com.orion.erp_back.user.exception.UserNotFoundException
import com.orion.erp_back.user.service.impl.ChangeUserServiceImpl
import com.orion.erp_back.user.service.impl.GetUserServiceImpl
import org.instancio.Instancio
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mockito
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.any
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import org.springframework.test.web.servlet.setup.DefaultMockMvcBuilder
import org.springframework.test.web.servlet.setup.MockMvcBuilders

@ActiveProfiles("test")
@Tag("integration-test")
@DisplayName("integration - User Controller Test")
@WebMvcTest(
    UserController::class
)
@ExtendWith(MockitoExtension::class)
class UserIntegrationControllerTests : SecurityItem() {
    private val user: User = Instancio.create(User::class.java)

    private val defaultPageable = Pageable.ofSize(1)

    @MockBean
    private lateinit var getUserServiceImpl: GetUserServiceImpl

    @MockBean
    private lateinit var changeUserServiceImpl: ChangeUserServiceImpl

    private val defaultUserEmail = "myemail@gmail.com"

    private val defaultUserPassword = "test_password_123!@"

    private val defaultAccessToken =
        "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c\n" +  //
                ""

    @BeforeEach
    fun setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
            .apply<DefaultMockMvcBuilder>(SecurityMockMvcConfigurers.springSecurity())
            .alwaysDo<DefaultMockMvcBuilder>(MockMvcResultHandlers.print()).build()
    }

    @Nested
    @DisplayName("GET /api/v1/users/{userId} Test")
    inner class GetUserByIdTest {
        @Test
        @DisplayName("GET /api/v1/users/{userId} Response")
        @WithMockCustomUser
        @Throws(
            Exception::class
        )
        fun `should expect OK Response to GetUserResponse when given userId and User is Authenticated`() {
            Mockito.`when`(getUserServiceImpl.getUserById(any<Long>()))
                .thenReturn(GetUserResponse.of(user))

            mockMvc.perform(
                MockMvcRequestBuilders.get("/api/v1/users/{userId}", user.id)
                    .with(SecurityMockMvcRequestPostProcessors.csrf())
                    .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)
            ).andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(commonMessage))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.userId").value(user.id))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.email").value(user.email))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.username").value(user.username))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.role").value(user.role.name))
        }

        @Test
        @DisplayName("Not Found Exception GET /api/v1/users/{userId} Response")
        @WithMockCustomUser
        @Throws(
            Exception::class
        )
        fun `should expect Error Response to UserNotFoundException when given userId and User is Authenticated`() {
            val userNotFoundException = UserNotFoundException(
                user.id
            )

            Mockito.`when`(getUserServiceImpl.getUserById(any<Long>()))
                .thenThrow(userNotFoundException)

            mockMvc.perform(
                MockMvcRequestBuilders.get("/api/v1/users/{userId}", user.id)
                    .with(SecurityMockMvcRequestPostProcessors.csrf())
                    .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)
            ).andExpect(MockMvcResultMatchers.status().isNotFound()).andExpect(
                MockMvcResultMatchers.jsonPath("$.message").value(userNotFoundException.message)
            ).andExpect(MockMvcResultMatchers.jsonPath("$.errors").isEmpty())
        }

        @Test
        @DisplayName("Unauthorized Exception GET /api/v1/users/{userId} Response")
        @Throws(
            Exception::class
        )
        fun `should expect Error Response to UnauthorizedException when given userId and User is Not Authenticated`() {
            mockMvc.perform(
                MockMvcRequestBuilders.get("/api/v1/users/{userId}", user.id)
                    .with(SecurityMockMvcRequestPostProcessors.csrf())
                    .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)
            ).andExpect(MockMvcResultMatchers.status().isUnauthorized())
        }
    }

    @Nested
    @DisplayName("GET /api/v1/users Test")
    inner class GetUserListTest {
        @Test
        @DisplayName("GET /api/v1/users Response")
        @WithMockCustomUser
        @Throws(
            Exception::class
        )
        fun `should expect OK Response to page of GetUserResponse when given Default Pageable and User is Authenticated`() {
            Mockito.`when`(getUserServiceImpl.getUserList(any<Pageable>()))
                .thenReturn(PageImpl(listOf(GetUserResponse.of(user)), defaultPageable, 1))

            mockMvc.perform(
                MockMvcRequestBuilders.get("/api/v1/users")
                    .with(SecurityMockMvcRequestPostProcessors.csrf())
                    .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)
            ).andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(commonMessage))
                .andExpect(
                    MockMvcResultMatchers.jsonPath("$.data.content[0].userId").value(user.id)
                ).andExpect(
                    MockMvcResultMatchers.jsonPath("$.data.content[0].email").value(user.email)
                ).andExpect(
                    MockMvcResultMatchers.jsonPath("$.data.content[0].username").value(user.username)
                ).andExpect(
                    MockMvcResultMatchers.jsonPath("$.data.content[0].role").value(user.role.name)
                )
        }

        @Test
        @DisplayName("Empty GET /api/v1/users Response")
        @WithMockCustomUser
        @Throws(
            Exception::class
        )
        fun `should expect OK Response to page of GetUserResponse is Empty when given Default Pageable And User is Authenticated`() {
            Mockito.`when`(getUserServiceImpl.getUserList(any<Pageable>()))
                .thenReturn(PageImpl(listOf(), defaultPageable, 0))

            mockMvc.perform(
                MockMvcRequestBuilders.get("/api/v1/users")
                    .with(SecurityMockMvcRequestPostProcessors.csrf())
                    .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)
            ).andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(commonMessage))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.content").isEmpty())
        }

        @Test
        @DisplayName("Unauthorized Exception GET /api/v1/users Response")
        @Throws(
            Exception::class
        )
        fun `should expect Error Response to UnauthorizedException when given Default Pageable and User is Not Authenticated`() {
            mockMvc.perform(
                MockMvcRequestBuilders.get("/api/v1/users")
                    .with(SecurityMockMvcRequestPostProcessors.csrf())
                    .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)
            ).andExpect(MockMvcResultMatchers.status().isUnauthorized())
        }
    }

    @Nested
    @DisplayName("POST /api/v1/users/register Test")
    inner class CreateUserTest {
        private val mockCreateUserRequest: CreateUserRequest = Instancio.create(
            CreateUserRequest::class.java
        )
        private val createUserRequest: CreateUserRequest = mockCreateUserRequest.copy(
            email = defaultUserEmail, password = defaultUserPassword
        )

        @Test
        @DisplayName("POST /api/v1/users/register Response")
        @WithMockCustomUser
        @Throws(
            Exception::class
        )
        fun `should expect OK Response to CreateUserResponse when given CreateUserRequest`() {
            Mockito.`when`(changeUserServiceImpl.createUser(any<CreateUserRequest>()))
                .thenReturn(CreateUserResponse.of(user, defaultAccessToken))

            mockMvc.perform(
                MockMvcRequestBuilders.post("/api/v1/users/register")
                    .with(SecurityMockMvcRequestPostProcessors.csrf())
                    .content(objectMapper.writeValueAsString(createUserRequest))
                    .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)
            ).andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(commonStatus))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(commonMessage))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.userId").value(user.id))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.email").value(user.email))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.username").value(user.username))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.role").value(user.role.name))
                .andExpect(
                    MockMvcResultMatchers.jsonPath("$.data.accessToken").value(defaultAccessToken)
                )
        }

        @Test
        @DisplayName("Field Valid Exception POST /api/v1/users/register Response")
        @WithMockCustomUser
        @Throws(
            Exception::class
        )
        fun `should expect Error Response  to ValidException when given wrong CreateUserRequest`() {
            val wrongCreateUserRequest = createUserRequest.copy(
                email = "wrong_email", password = "1234567"
            )

            mockMvc.perform(
                MockMvcRequestBuilders.post("/api/v1/users/register")
                    .with(SecurityMockMvcRequestPostProcessors.csrf())
                    .content(objectMapper.writeValueAsString(wrongCreateUserRequest))
                    .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)
            ).andExpect(MockMvcResultMatchers.status().isBadRequest()).andExpect(
                MockMvcResultMatchers.jsonPath("$.code").value(HttpStatus.BAD_REQUEST.value())
            ) // email:field email is not email format, password:field password is min size 8 and max size 20,
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").isString())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors").isNotEmpty())
        }

        @Test
        @DisplayName("Already Exist Exception POST /api/v1/users/register Response")
        @WithMockCustomUser
        @Throws(
            Exception::class
        )
        fun `should expect Error Response to AlreadyUserExistException when given CreateUserRequest`() {
            val alreadyUserExistException = AlreadyUserExistException(
                createUserRequest.email
            )

            Mockito.`when`(changeUserServiceImpl.createUser(any<CreateUserRequest>()))
                .thenThrow(alreadyUserExistException)

            mockMvc.perform(
                MockMvcRequestBuilders.post("/api/v1/users/register")
                    .with(SecurityMockMvcRequestPostProcessors.csrf())
                    .content(objectMapper.writeValueAsString(createUserRequest))
                    .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)
            ).andExpect(MockMvcResultMatchers.status().isConflict()).andExpect(
                MockMvcResultMatchers.jsonPath("$.message").value(alreadyUserExistException.message)
            ).andExpect(MockMvcResultMatchers.jsonPath("$.errors").isEmpty())
        }
    }

    @Nested
    @DisplayName("PATCH /api/v1/users/{userId} Test")
    inner class UpdateUserTest {
        private val updateUserRequest: UpdateUserRequest = Instancio.create(
            UpdateUserRequest::class.java
        )

        @Test
        @DisplayName("PATCH /api/v1/users/{userId} Response")
        @WithMockCustomUser
        @Throws(
            Exception::class
        )
        fun `should expect OK Response to UpdateUserResponse when given userId and UpdateUserRequest and User is Authenticated`() {
            Mockito.`when`(changeUserServiceImpl.updateUser(any<Long>(), any<UpdateUserRequest>()))
                .thenReturn(UpdateUserResponse.of(user))

            mockMvc.perform(
                MockMvcRequestBuilders.patch("/api/v1/users/{userId}", user.id)
                    .with(SecurityMockMvcRequestPostProcessors.csrf())
                    .content(objectMapper.writeValueAsString(updateUserRequest))
                    .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)
            ).andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(commonStatus))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(commonMessage))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.userId").value(user.id))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.email").value(user.email))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.username").value(user.username))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.role").value(user.role.name))
        }

        @Test
        @DisplayName("Field Valid Exception PATCH /api/v1/users/{userId} Response")
        @WithMockCustomUser
        @Throws(
            Exception::class
        )
        fun `should expect Error Response to ValidException when given userId and wrong UpdateUserRequest and User is Authenticated`() {
            val wrongUpdateUserRequest = updateUserRequest.copy(
                username = "",
            )

            mockMvc.perform(
                MockMvcRequestBuilders.patch("/api/v1/users/{userId}", user.id)
                    .with(SecurityMockMvcRequestPostProcessors.csrf())
                    .content(objectMapper.writeValueAsString(wrongUpdateUserRequest))
                    .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)
            ).andExpect(MockMvcResultMatchers.status().isBadRequest())
                // name:field name is blank
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").isString())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors").isNotEmpty())
        }

        @Test
        @DisplayName("Unauthorized Exception PATCH /api/v1/users/{userId} Response")
        @Throws(
            Exception::class
        )
        fun `should expect ErrorResponse to UnauthorizedException when given userId and UpdateUserRequest and User is Not Authenticated`() {
            mockMvc.perform(
                MockMvcRequestBuilders.patch("/api/v1/users/{userId}", user.id)
                    .with(SecurityMockMvcRequestPostProcessors.csrf())
                    .content(objectMapper.writeValueAsString(updateUserRequest))
                    .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)
            ).andExpect(MockMvcResultMatchers.status().isUnauthorized())
        }

        @Test
        @DisplayName("Not Found Exception PATCH /api/v1/users/{userId} Response")
        @WithMockCustomUser
        @Throws(
            Exception::class
        )
        fun `should expect Error Response to UserNotFoundException when given userId and UpdateUserRequest and User is Authenticated`() {
            val userNotFoundException = UserNotFoundException(
                user.id
            )

            Mockito.`when`(changeUserServiceImpl.updateUser(any<Long>(), any<UpdateUserRequest>()))
                .thenThrow(userNotFoundException)

            mockMvc.perform(
                MockMvcRequestBuilders.patch("/api/v1/users/{userId}", user.id)
                    .with(SecurityMockMvcRequestPostProcessors.csrf())
                    .content(objectMapper.writeValueAsString(updateUserRequest))
                    .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)
            ).andExpect(MockMvcResultMatchers.status().isNotFound()).andExpect(
                MockMvcResultMatchers.jsonPath("$.message").value(userNotFoundException.message)
            ).andExpect(MockMvcResultMatchers.jsonPath("$.errors").isEmpty())
        }
    }

    @Nested
    @DisplayName("PATCH /api/v1/users Test")
    inner class UpdateMeTest {
        private val updateUserRequest: UpdateUserRequest = Instancio.create(
            UpdateUserRequest::class.java
        )

        @Test
        @DisplayName("PATCH /api/v1/users Response")
        @WithMockCustomUser
        @Throws(
            Exception::class
        )
        fun `should expect OK Response to UpdateMeResponse when given SecurityUserItem and UpdateUserRequest and User is Authenticated`() {
            Mockito.`when`(changeUserServiceImpl.updateMe(any<Long>(), any<UpdateUserRequest>()))
                .thenReturn(UpdateMeResponse.of(user, defaultAccessToken))

            mockMvc.perform(
                MockMvcRequestBuilders.patch("/api/v1/users")
                    .with(SecurityMockMvcRequestPostProcessors.csrf())
                    .content(objectMapper.writeValueAsString(updateUserRequest))
                    .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)
            ).andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(commonStatus))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(commonMessage))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.userId").value(user.id))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.email").value(user.email))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.username").value(user.username))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.role").value(user.role.name))
        }

        @Test
        @DisplayName("Field Valid Exception PATCH /api/v1/users Response")
        @WithMockCustomUser
        @Throws(
            Exception::class
        )
        fun `should expect Error Response to ValidException when given SecurityUserItem and wrong UpdateUserRequest and User is Authenticated`() {
            val wrongUpdateUserRequest = updateUserRequest.copy(
                username = "",
            )

            mockMvc.perform(
                MockMvcRequestBuilders.patch("/api/v1/users")
                    .with(SecurityMockMvcRequestPostProcessors.csrf())
                    .content(objectMapper.writeValueAsString(wrongUpdateUserRequest))
                    .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)
            ).andExpect(MockMvcResultMatchers.status().isBadRequest()).andExpect(
                MockMvcResultMatchers.jsonPath("$.code").value(HttpStatus.BAD_REQUEST.value())
            ) // name:field name is blank
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").isString())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors").isNotEmpty())
        }

        @Test
        @DisplayName("Unauthorized Exception PATCH /api/v1/users Response")
        @Throws(
            Exception::class
        )
        fun `should expect Error Response to UnauthorizedException when given UpdateUserRequest and User is Not Authenticated`() {
            mockMvc.perform(
                MockMvcRequestBuilders.patch("/api/v1/users")
                    .with(SecurityMockMvcRequestPostProcessors.csrf())
                    .content(objectMapper.writeValueAsString(updateUserRequest))
                    .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)
            ).andExpect(MockMvcResultMatchers.status().isUnauthorized())
        }

        @Test
        @DisplayName("Not Found Exception PATCH /api/v1/users Response")
        @WithMockCustomUser
        @Throws(
            Exception::class
        )
        fun `should expect Error Response to UserNotFoundException when given SecurityUserItem and UpdateUserRequest and User is Authenticated`() {
            val userNotFoundException = UserNotFoundException(
                user.id
            )

            Mockito.`when`(changeUserServiceImpl.updateMe(any<Long>(), any<UpdateUserRequest>()))
                .thenThrow(userNotFoundException)

            mockMvc.perform(
                MockMvcRequestBuilders.patch("/api/v1/users")
                    .with(SecurityMockMvcRequestPostProcessors.csrf())
                    .content(objectMapper.writeValueAsString(updateUserRequest))
                    .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)
            ).andExpect(MockMvcResultMatchers.status().isNotFound()).andExpect(
                MockMvcResultMatchers.jsonPath("$.message").value(userNotFoundException.message)
            ).andExpect(MockMvcResultMatchers.jsonPath("$.errors").isEmpty())
        }
    }

    @Nested
    @DisplayName("DELETE /api/v1/users/{userId} Test")
    inner class DeleteUserTest {
        @Test
        @DisplayName("DELETE /api/v1/users/{userId} Response")
        @WithMockCustomUser
        @Throws(
            Exception::class
        )
        fun `should expect OK Response when given userId and User is Authenticated`() {
            mockMvc.perform(
                MockMvcRequestBuilders.delete("/api/v1/users/{userId}", user.id)
                    .with(SecurityMockMvcRequestPostProcessors.csrf())
                    .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)
            ).andExpect(MockMvcResultMatchers.status().isNoContent())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(commonMessage))
        }

        @Test
        @DisplayName("Unauthorized Error DELETE /api/v1/users/{userId} Response")
        @Throws(
            Exception::class
        )
        fun `should expect Error Response to UnauthorizedException when given userId and User is Not Authenticated`() {
            mockMvc.perform(
                MockMvcRequestBuilders.delete("/api/v1/users/{userId}", user.id)
                    .with(SecurityMockMvcRequestPostProcessors.csrf())
                    .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)
            ).andExpect(MockMvcResultMatchers.status().isUnauthorized())
        }
    }
}
