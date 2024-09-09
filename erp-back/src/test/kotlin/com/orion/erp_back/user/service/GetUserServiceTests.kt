package com.orion.erp_back.user.service

import com.orion.erp_back.user.entity.User
import com.orion.erp_back.user.exception.UserNotFoundException
import com.orion.erp_back.user.repository.UserRepository
import com.orion.erp_back.user.service.impl.GetUserServiceImpl
import org.assertj.core.api.Assertions.assertThat
import org.instancio.Instancio
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Assertions.assertThrows
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
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.test.context.ActiveProfiles

@ActiveProfiles("test")
@Tag("unit-test")
@DisplayName("Unit - Get User Service Test")
@ExtendWith(
    MockitoExtension::class
)
class GetUserServiceTests {
    @Mock
    private lateinit var userRepository: UserRepository

    @InjectMocks
    private lateinit var getUserServiceImpl: GetUserServiceImpl

    private val user: User = Instancio.create(User::class.java)

    private val defaultPageable = Pageable.ofSize(1)

    @Nested
    @DisplayName("Get User By Id Test")
    inner class GetUserByIdTest {
        @Test
        @DisplayName("Success get user by id")
        fun `should assert GetUserResponse when given userId`() {
            Mockito.`when`(userRepository.findOneById(any<Long>())).thenReturn(user)

            val getUserResponse = getUserServiceImpl.getUserById(
                user.id
            )

            assertNotNull(getUserResponse)
            assertEquals(user.id, getUserResponse.userId)
            assertEquals(user.email, getUserResponse.email)
            assertEquals(user.username, getUserResponse.username)
            assertEquals(user.role, getUserResponse.role)
        }

        @Test
        @DisplayName("Not found user")
        fun `should assert UserNotFoundException when given userId`() {
            Mockito.`when`(userRepository.findOneById(any<Long>()))
                .thenThrow(UserNotFoundException(user.id))

            assertThrows(
                UserNotFoundException::class.java
            ) { getUserServiceImpl.getUserById(user.id) }
        }
    }

    @Nested
    @DisplayName("Get User By Email Test")
    inner class GetUserByEmailTest {
        @Test
        @DisplayName("Success get user by email")
        fun `should assert GetUserResponse when given User Email`() {
            Mockito.`when`(userRepository.findOneByEmail(any<String>())).thenReturn(user)

            val getUserResponse = requireNotNull(
                getUserServiceImpl.getUserByEmail(
                    user.email
                )
            ) {
                "Get user response must not be null"
            }

            assertNotNull(getUserResponse)
            assertEquals(user.id, getUserResponse.userId)
            assertEquals(user.email, getUserResponse.email)
            assertEquals(user.username, getUserResponse.username)
            assertEquals(user.role, getUserResponse.role)
        }

        @Test
        @DisplayName("Get user by email is null")
        fun `should assert GetUserResponse is Null when given User Email`() {
            Mockito.`when`(userRepository.findOneByEmail(any<String>())).thenReturn(null)

            val getUserResponse = getUserServiceImpl.getUserByEmail(
                user.email
            )

            assertNull(getUserResponse)
        }
    }

    @Nested
    @DisplayName("Get User List Test")
    inner class GetUserListTest {
        @Test
        @DisplayName("Success get user list")
        fun `should assert page of GetUserResponse when given Default Pageable`() {
            val userList: Page<User> = PageImpl(listOf(user), defaultPageable, 1)

            Mockito.`when`(userRepository.findAll(any<Pageable>())).thenReturn(userList)

            val getUserResponseList = getUserServiceImpl.getUserList(
                defaultPageable
            )

            assertThat(getUserResponseList).isNotEmpty()
            assertEquals(getUserResponseList.content[0].email, user.email)
            assertEquals(getUserResponseList.content[0].username, user.username)
            assertEquals(getUserResponseList.content[0].role, user.role)
        }

        @Test
        @DisplayName("Get user list is empty")
        fun `should assert page of GetUserResponse is Empty when given Default Pageable`() {
            val emptyUserList: Page<User> = PageImpl(listOf(), defaultPageable, 0)

            Mockito.`when`(userRepository.findAll(any<Pageable>())).thenReturn(emptyUserList)

            val getUserResponseList = getUserServiceImpl.getUserList(
                defaultPageable
            )

            assertThat(getUserResponseList).isEmpty()
            assertEquals(getUserResponseList.totalElements, 0)
        }
    }
}
