package com.orion.erp_back.security

import com.orion.erp_back.user.constant.UserRole
import com.orion.erp_back.user.entity.User

data class SecurityUserItem(
    val userId: Long,
    val role: UserRole,
    val username: String,
    val email: String,
) {
    companion object {
        fun of(user: User): SecurityUserItem {
            return SecurityUserItem(
                userId = user.id,
                role = user.role,
                username = user.username,
                email = user.email
            )
        }
    }
}