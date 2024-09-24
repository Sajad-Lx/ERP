package com.orion.erp_back.security.component.provider

import com.orion.erp_back.security.SecurityUserItem
import com.orion.erp_back.security.exception.RefreshTokenNotFoundException
import com.orion.erp_back.user.entity.User
import com.orion.erp_back.utils.RedisUtils
import org.springframework.stereotype.Component
import java.util.concurrent.TimeUnit

@Component
class TokenProvider(
    private val jwtProvider: JWTProvider,
    private val redisUtils: RedisUtils,
) {
    fun getRefreshToken(userId: Long): String {
        val redisKey: String = redisUtils.generateSessionKey(userId)
        val refreshToken: String = redisUtils.get(redisKey) ?: throw RefreshTokenNotFoundException(userId)
        println(refreshToken)

        return refreshToken
    }

    fun deleteRefreshToken(userId: Long) {
        val redisKey: String = redisUtils.generateSessionKey(userId)
        redisUtils.delete(redisKey)
    }

    fun createRefreshToken(user: User) {
        val redisKey: String = redisUtils.generateSessionKey(user.id)

        redisUtils.set(
            redisKey,
            jwtProvider.createRefreshToken(SecurityUserItem.of(user)),
            jwtProvider.refreshExpireTime,
            TimeUnit.MILLISECONDS,
        )
    }

    fun createAccessToken(user: User): String = jwtProvider.createAccessToken(SecurityUserItem.of(user))

    fun refreshAccessToken(securityUserItem: SecurityUserItem): String = jwtProvider.refreshAccessToken(
        securityUserItem,
        getRefreshToken(securityUserItem.userId)
    )

    fun createFullTokens(user: User): String {
        createRefreshToken(user)
        return createAccessToken(user)
    }
}