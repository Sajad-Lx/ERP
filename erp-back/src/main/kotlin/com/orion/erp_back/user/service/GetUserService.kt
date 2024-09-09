package com.orion.erp_back.user.service

import com.orion.erp_back.user.dto.serve.response.GetUserResponse
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

interface GetUserService {
    fun getUserById(userId: Long): GetUserResponse

    fun getUserByEmail(email: String): GetUserResponse?

    fun getUserByUsername(username: String): GetUserResponse?

    fun getUserList(pageable: Pageable): Page<GetUserResponse>
}