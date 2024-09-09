package com.orion.erp_back.user.service.impl

import com.orion.erp_back.user.dto.serve.response.GetUserResponse
import com.orion.erp_back.user.entity.User
import com.orion.erp_back.user.exception.UserNotFoundException
import com.orion.erp_back.user.repository.UserRepository
import com.orion.erp_back.user.service.GetUserService
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class GetUserServiceImpl(
    private val userRepository: UserRepository,
) : GetUserService {
    override fun getUserById(userId: Long): GetUserResponse {
        val user: User = userRepository.findOneById(userId) ?: throw UserNotFoundException(userId)

        return user.let(GetUserResponse::of)
    }

    override fun getUserByEmail(email: String): GetUserResponse? {
        val user: User = userRepository.findOneByEmail(email) ?: return null

        return user.let(GetUserResponse::of)
    }

    override fun getUserByUsername(username: String): GetUserResponse? {
        val user: User = userRepository.findOneByUsername(username) ?: return null

        return user.let(GetUserResponse::of)
    }

    override fun getUserList(pageable: Pageable): Page<GetUserResponse> {
        return userRepository.findAll(pageable).map(GetUserResponse::of)
    }
}