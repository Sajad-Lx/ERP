package com.orion.erp_back.user.repository

import com.orion.erp_back.common.config.JpaAuditConfig
import com.orion.erp_back.user.constant.UserRole
import com.orion.erp_back.user.dto.serve.request.UpdateUserRequest
import com.orion.erp_back.user.entity.User
import org.instancio.Instancio
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.context.annotation.Import
import org.springframework.test.context.ActiveProfiles

@ActiveProfiles("test")
@Tag("unit-test")
@DisplayName("Unit - User Repository Test")
@Import(value = [JpaAuditConfig::class])
@DataJpaTest
class UserRepositoryTests(
    @Autowired private val userRepository: UserRepository,
) {
    private val defaultUserEmail = "myemail@gmail.com"

    private val defaultUserPassword = "test_password_123!@"

    private val defaultUserName = "OrionXD"

    private val defaultFirstName = "Orion"

    private val defaultLastName = "XD"

    private val defaultUserRole = UserRole.STANDARD_USER

    private lateinit var userEntity: User

    @BeforeEach
    @Throws(Exception::class)
    fun setUp() {
        userEntity = User(
            email = defaultUserEmail,
            username = defaultUserName,
            password = defaultUserPassword,
            firstName = defaultFirstName,
            lastName = defaultLastName,
            role = defaultUserRole
        )
    }

    @Test
    @DisplayName("Create user")
    fun `should assert createdUser entity when given User entity`() {
        val createUser = userRepository.save(userEntity)

        assertEquals(createUser.id, userEntity.id)
        assertEquals(createUser.email, userEntity.email)
        assertEquals(createUser.username, userEntity.username)
        assertEquals(createUser.firstName, userEntity.firstName)
        assertEquals(createUser.lastName, userEntity.lastName)
        assertEquals(createUser.role, userEntity.role)
    }

    @Test
    @DisplayName("Update user")
    fun `should assert updated User entity when given userId and update User request`() {
        val updateUserRequest = Instancio.create(
            UpdateUserRequest::class.java
        )

        val beforeUpdateUser = userRepository.save(userEntity)

        userRepository.save(
            beforeUpdateUser.update(
                updateUserRequest.username,
                updateUserRequest.firstName,
                updateUserRequest.lastName,
                updateUserRequest.role
            )
        )

        val afterUpdateUser: User = requireNotNull(
            userRepository.findOneById(beforeUpdateUser.id)
        ) {
            "User must not be null"
        }

        assertEquals(afterUpdateUser.username, updateUserRequest.username)
        assertEquals(afterUpdateUser.firstName, updateUserRequest.firstName)
        assertEquals(afterUpdateUser.lastName, updateUserRequest.lastName)
        assertEquals(afterUpdateUser.role, updateUserRequest.role)
    }

    @Test
    @DisplayName("Delete user")
    fun `should assert deleted User entity when given userId`() {
        val beforeDeleteUser = userRepository.save(userEntity)

        userRepository.deleteById(beforeDeleteUser.id)

        val afterDeleteUser: User? = userRepository.findOneById(beforeDeleteUser.id)

        assertNull(afterDeleteUser)
    }

    @Test
    @DisplayName("Find user by id")
    fun `should assert find User entity when given userId`() {
        val beforeFindUser = userRepository.save(userEntity)

        val afterFindUser: User = requireNotNull(
            userRepository.findOneById(beforeFindUser.id)
        ) {
            "User must not be null"
        }

        assertEquals(beforeFindUser.id, afterFindUser.id)
        assertEquals(beforeFindUser.email, afterFindUser.email)
        assertEquals(beforeFindUser.username, afterFindUser.username)
        assertEquals(beforeFindUser.firstName, afterFindUser.firstName)
        assertEquals(beforeFindUser.lastName, afterFindUser.lastName)
        assertEquals(beforeFindUser.role, afterFindUser.role)
    }

    @Test
    @DisplayName("Find user by email")
    fun `should assert find User entity when given User Email`() {
        val beforeFindUser = userRepository.save(userEntity)

        val afterFindUser: User = requireNotNull(
            userRepository.findOneByEmail(beforeFindUser.email)
        ) {
            "User must not be null"
        }

        assertEquals(beforeFindUser.id, afterFindUser.id)
        assertEquals(beforeFindUser.email, afterFindUser.email)
        assertEquals(beforeFindUser.username, afterFindUser.username)
        assertEquals(beforeFindUser.firstName, afterFindUser.firstName)
        assertEquals(beforeFindUser.lastName, afterFindUser.lastName)
        assertEquals(beforeFindUser.role, afterFindUser.role)
    }
}
