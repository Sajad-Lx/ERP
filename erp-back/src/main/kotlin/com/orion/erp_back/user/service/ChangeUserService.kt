package com.orion.erp_back.user.service

import com.orion.erp_back.user.dto.serve.request.CreateUserRequest
import com.orion.erp_back.user.dto.serve.request.UpdateUserRequest
import com.orion.erp_back.user.dto.serve.response.CreateUserResponse
import com.orion.erp_back.user.dto.serve.response.UpdateMeResponse
import com.orion.erp_back.user.dto.serve.response.UpdateUserResponse

interface ChangeUserService {
    fun updateUser(
        userId: Long,
        updateUserRequest: UpdateUserRequest
    ): UpdateUserResponse

    fun updateMe(
        userId: Long,
        updateUserRequest: UpdateUserRequest
    ): UpdateMeResponse

    fun createUser(createUserRequest: CreateUserRequest): CreateUserResponse

    fun deleteUser(userId: Long)
}