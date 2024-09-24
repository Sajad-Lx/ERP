package com.orion.erp_back.security.config

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Positive
import jakarta.validation.constraints.Size
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.validation.annotation.Validated

/**
 * Configuration properties for JWT security settings.
 *
 * These properties are loaded from the application configuration (e.g., application.yml or application.properties)
 * under the prefix "jwt-security".
 *
 * @property secret The secret key used for signing JWTs. It must be at least 64 characters long.
 * @property accessExpirationTime The expiration time for JWTs in days. It must be a positive integer.
 * @property strength The strength parameter used in password encoding, typically defining the number of hashing rounds.
 * @property tokenPrefix The prefix used for the JWT token in the Authorization header (constant value "Bearer ").
 * @property headerString The name of the HTTP header where the JWT token is expected (constant value "Authorization").
 */
@ConfigurationProperties(prefix = "jwt-security")
@Validated
class SecurityProperties {
    @field:NotBlank
    @field:Size(min = 64)
    var secret: String = ""

    @field:NotBlank
    @field:Size(min = 36)
    var apiKey: String = ""

    @field:Positive
    var accessExpirationTime: Long = 60_000 // 10 min in milliseconds

    @field:Positive
    var refreshExpirationTime: Long = 300_000// 7 * 24 * 60 * 60 * 1000 // 7 Days

    @field:Positive
    var strength: Int = 10

    // Constants for JWT token handling
    val tokenPrefix: String = "Bearer "
    val headerString: String = "Authorization"
}