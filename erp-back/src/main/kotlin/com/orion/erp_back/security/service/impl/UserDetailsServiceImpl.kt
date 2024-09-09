package com.orion.erp_back.security.service.impl

import com.orion.erp_back.security.SecurityUserItem
import com.orion.erp_back.security.UserAdapter
import com.orion.erp_back.user.entity.User
import com.orion.erp_back.user.exception.UserNotFoundException
import com.orion.erp_back.user.service.UserService
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service

@Service
class UserDetailsServiceImpl(
    private val userService: UserService
) : UserDetailsService {

    @Throws(UserNotFoundException::class)
    override fun loadUserByUsername(userId: String): UserDetails {
        val user: User = userService.validateReturnUser(userId.toLong())

        return UserAdapter(SecurityUserItem.of(user))
    }
}