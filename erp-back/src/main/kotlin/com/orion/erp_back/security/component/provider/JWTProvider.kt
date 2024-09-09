package com.orion.erp_back.security.component.provider

import com.orion.erp_back.security.SecurityUserItem
import com.orion.erp_back.security.UserAdapter
import com.orion.erp_back.security.config.SecurityProperties
import com.orion.erp_back.security.service.impl.UserDetailsServiceImpl
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import jakarta.annotation.PostConstruct
import jakarta.servlet.http.HttpServletRequest
import org.springframework.http.HttpHeaders
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.stereotype.Component
import java.util.Date
import javax.crypto.SecretKey

@Component
class JWTProvider(
    private val userDetailsServiceImpl: UserDetailsServiceImpl,
    private val securityProperties: SecurityProperties,
) {
    private var key: SecretKey? = null

    val accessExpireTime: Long = securityProperties.accessExpirationTime

    val refreshExpireTime: Long = securityProperties.refreshExpirationTime

    /**
     * Initializes the secret key used for signing JWT tokens.
     *
     * This method is called after the bean is constructed, converting the secret string from the security properties into a `SecretKey`.
     */
    @PostConstruct
    fun init() {
        key = Keys.hmacShaKeyFor(securityProperties.secret.toByteArray())
    }

    fun createAccessToken(securityUserItem: SecurityUserItem): String {
        return generateToken(securityUserItem, true)
    }

    fun createRefreshToken(securityUserItem: SecurityUserItem): String {
        return generateToken(securityUserItem, false)
    }

    /**
     * Creates a JWT token for the authenticated user.
     *
     * The token includes the username and authorities (roles) of the user, and is set to expire based on the configuration properties.
     *
     * @param securityUserItem The securityUserItem object containing the user's details and authorities.
     * @param isAccessToken Boolean is used to identify if its generating accessToken or refreshToken.
     * @return String The generated JWT token as a string.
     */
    fun generateToken(
        securityUserItem: SecurityUserItem,
        isAccessToken: Boolean,
    ): String {
        val expireTime =
            if (isAccessToken) accessExpireTime else refreshExpireTime
        val expiration = Date(System.currentTimeMillis() + expireTime)

        val authClaims: MutableList<String> = mutableListOf()
        authClaims.add(securityUserItem.email)
        authClaims.add(securityUserItem.role.toString())

        return Jwts.builder().subject(securityUserItem.userId.toString()).claim("auth", authClaims)
            .issuedAt(Date()).expiration(expiration).signWith(key).compact()
    }

    fun refreshAccessToken(
        securityUserItem: SecurityUserItem,
        refreshToken: String,
    ): String {
        generateClaims(refreshToken)
        return createAccessToken(securityUserItem)
    }

    fun validateToken(token: String): Boolean {
        val claims: Claims = generateClaims(token)
        return !claims.isEmpty()
    }

    fun generateRequestToken(request: HttpServletRequest): String? {
        val token = request.getHeader(HttpHeaders.AUTHORIZATION)
            ?.takeIf { it.startsWith(securityProperties.tokenPrefix) }?.substring(7)

        return token
    }

    fun getAuthentication(token: String): UsernamePasswordAuthenticationToken {
        val claims: Claims = generateClaims(token)
        val userAdapter: UserAdapter = userDetailsServiceImpl.loadUserByUsername(
            claims.subject
        ) as UserAdapter

        return UsernamePasswordAuthenticationToken(
            userAdapter,
            null,
            userAdapter.authorities,
        )
    }


    private fun generateClaims(token: String): Claims {
        return Jwts.parser().verifyWith(key).clockSkewSeconds(3 * 60).build()
            .parseSignedClaims(token.replace(securityProperties.tokenPrefix, "")).payload
    }
}