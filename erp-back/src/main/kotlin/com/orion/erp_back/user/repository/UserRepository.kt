package com.orion.erp_back.user.repository

import com.orion.erp_back.user.entity.User
import org.springframework.data.jpa.repository.JpaRepository

interface UserRepository: JpaRepository<User, Long> {
    fun findOneById(userId: Long): User?

    fun findOneByUsername(username: String): User?

    fun findOneByEmail(email: String): User?

    fun existsByEmail(email: String): Boolean
}