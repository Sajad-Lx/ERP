package com.orion.erp_back.user.service

import com.orion.erp_back.auth.dto.serve.request.SignInRequest
import com.orion.erp_back.auth.dto.serve.request.TwoFaVerifyRequest
import com.orion.erp_back.user.entity.User

interface UserService {
    fun validateReturnUser(userId: Long): User

    fun validateAuthReturnUser(signInRequest: SignInRequest): User

    fun validate2Fa(twoFaVerifyRequest: TwoFaVerifyRequest): User
}