package com.orion.erp_back.user.service.impl

import com.orion.erp_back.security.component.provider.TokenProvider
import com.orion.erp_back.user.constant.UserRole
import com.orion.erp_back.user.dto.serve.request.CreateUserRequest
import com.orion.erp_back.user.dto.serve.request.UpdateUserRequest
import com.orion.erp_back.user.dto.serve.response.CreateUserResponse
import com.orion.erp_back.user.dto.serve.response.UpdateMeResponse
import com.orion.erp_back.user.dto.serve.response.UpdateUserResponse
import com.orion.erp_back.user.entity.User
import com.orion.erp_back.user.exception.AlreadyUserExistException
import com.orion.erp_back.user.repository.UserRepository
import com.orion.erp_back.user.service.ChangeUserService
import com.orion.erp_back.user.service.UserService
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class ChangeUserServiceImpl(
    private val tokenProvider: TokenProvider,
    private val userService: UserService,
    private val userRepository: UserRepository,
    private val bCryptPasswordEncoder: BCryptPasswordEncoder,
) : ChangeUserService {
    override fun updateUser(
        userId: Long,
        updateUserRequest: UpdateUserRequest
    ): UpdateUserResponse {
        val user: User = userService
            .validateReturnUser(userId)
            .update(
                username = updateUserRequest.username,
                firstName = updateUserRequest.firstName,
                lastName = updateUserRequest.lastName,
                role = updateUserRequest.role
            )

        return user.let(UpdateUserResponse::of)
    }

    override fun updateMe(userId: Long, updateUserRequest: UpdateUserRequest): UpdateMeResponse {
        val user: User = userService
            .validateReturnUser(userId)
            .update(
                username = updateUserRequest.username,
                firstName = updateUserRequest.firstName,
                lastName = updateUserRequest.lastName,
                role = updateUserRequest.role
            )

        return UpdateMeResponse.of(user, tokenProvider.createFullTokens(user))
    }

    override fun createUser(createUserRequest: CreateUserRequest): CreateUserResponse {
        val isExist: Boolean = userRepository.existsByEmail(createUserRequest.email)
        if (isExist) {
            throw AlreadyUserExistException(createUserRequest.email)
        }

        val user: User = User(
            username = createUserRequest.username,
            email = createUserRequest.email,
            password = createUserRequest.password,
            firstName = createUserRequest.firstName,
            lastName = createUserRequest.lastName,
            role = UserRole.STANDARD_USER,
        ).encodePassword(bCryptPasswordEncoder)

        return CreateUserResponse.of(
            userRepository.save(user),
            tokenProvider.createFullTokens(user)
        )
    }

    override fun deleteUser(userId: Long) {
        tokenProvider.deleteRefreshToken(userId)
        userRepository.deleteById(userId)
    }
}