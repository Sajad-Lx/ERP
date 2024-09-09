package com.orion.erp_back.user.service.impl

import com.orion.erp_back.auth.dto.serve.request.SignInRequest
import com.orion.erp_back.auth.dto.serve.request.TwoFaVerifyRequest
import com.orion.erp_back.common.exception.ForbiddenException
import com.orion.erp_back.user.entity.User
import com.orion.erp_back.user.exception.UserNotFoundException
import com.orion.erp_back.user.exception.UserUnAuthorizedException
import com.orion.erp_back.user.repository.UserRepository
import com.orion.erp_back.user.service.UserService
import org.jboss.aerogear.security.otp.Totp
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service

@Service
class UserServiceImpl(
    private val userRepository: UserRepository,
    private val bCryptPasswordEncoder: BCryptPasswordEncoder,
) : UserService {
    override fun validateReturnUser(userId: Long): User {
        val user: User = userRepository.findOneById(userId) ?: throw UserNotFoundException(userId)
        return user
    }

    override fun validateAuthReturnUser(signInRequest: SignInRequest): User {
        val user: User =
            userRepository.findOneByEmail(signInRequest.email) ?: throw UserNotFoundException(
                signInRequest.email
            )

        val isValidate = user.validatePassword(
            signInRequest.password,
            bCryptPasswordEncoder,
        )

        if (!isValidate) {
            throw UserUnAuthorizedException(signInRequest.email)
        }

        return user
    }

    override fun validate2Fa(twoFaVerifyRequest: TwoFaVerifyRequest): User {
        val user: User =
            userRepository.findOneById(twoFaVerifyRequest.userId) ?: throw UserNotFoundException(
                twoFaVerifyRequest.userId
            )

        if (!user.isUsing2FA) {
            throw ForbiddenException("2FA is not enabled for this user.")
        }

        val totp = Totp(user.secret)

        when {
            !isValidLong(twoFaVerifyRequest.twoFaCode) -> throw BadCredentialsException("Invalid verification code")
            !totp.verify(twoFaVerifyRequest.twoFaCode) -> throw BadCredentialsException("Invalid verification code")
        }

        return user
    }

    private fun isValidLong(code: String): Boolean {
        return try {
            code.toLong()
            true
        } catch (e: NumberFormatException) {
            false
        }
    }
}