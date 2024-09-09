package com.orion.erp_back.security.component.provider

import com.orion.erp_back.security.component.filter.JWTAuthFilter
import com.orion.erp_back.security.config.CorsConfig
import com.orion.erp_back.security.config.SecurityProperties
import jakarta.servlet.http.HttpServletRequest
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configurers.CorsConfigurer
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer
import org.springframework.security.config.annotation.web.configurers.FormLoginConfigurer
import org.springframework.security.config.annotation.web.configurers.HttpBasicConfigurer
import org.springframework.security.config.annotation.web.configurers.SessionManagementConfigurer
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.stereotype.Component

@Component
class AuthProvider(
    private val corsConfig: CorsConfig,
    private val jwtProvider: JWTProvider,
    securityProperties: SecurityProperties,
) {
    var apiKey: String = securityProperties.apiKey

    fun ignoreListDefaultEndpoints(): Array<String> = arrayOf(
        "/api-docs/**",
        "/h2-console/**",
    )

    fun whiteListDefaultEndpoints(): Array<String> {
        return arrayOf(
            "/api/v1/auth/signIn",
            "/api/v1/users/register",
        )
    }

    fun generateRequestApiKey(request: HttpServletRequest): String? {
        return request.getHeader("X-API-KEY")
    }

    fun validateApiKey(requestApiKey: String): Boolean {
        return apiKey == requestApiKey
    }

    fun defaultSecurityFilterChain(httpSecurity: HttpSecurity): HttpSecurity =
        httpSecurity.csrf { csrf: CsrfConfigurer<HttpSecurity?> -> csrf.disable() }
            .httpBasic { httpBasic: HttpBasicConfigurer<HttpSecurity?> -> httpBasic.disable() }
            .formLogin { formLogin: FormLoginConfigurer<HttpSecurity?> -> formLogin.disable() }
            .cors { cors: CorsConfigurer<HttpSecurity?> ->
                cors.configurationSource(
                    corsConfig.corsConfigurationSource()
                )
            }.authorizeHttpRequests { request ->
                request.requestMatchers(*whiteListDefaultEndpoints()).permitAll().anyRequest()
                    .authenticated()
            }
            .sessionManagement { httpSecuritySessionManagementConfigurer: SessionManagementConfigurer<HttpSecurity?> ->
                httpSecuritySessionManagementConfigurer.sessionCreationPolicy(
                    SessionCreationPolicy.STATELESS
                )
            }.addFilterBefore(
                JWTAuthFilter(jwtProvider), UsernamePasswordAuthenticationFilter::class.java
            )
}