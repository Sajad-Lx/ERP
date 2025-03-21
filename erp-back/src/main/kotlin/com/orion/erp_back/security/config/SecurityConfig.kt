package com.orion.erp_back.security.config

import com.orion.erp_back.security.component.CustomAuthenticationEntryPoint
import com.orion.erp_back.security.component.filter.APIKeyAuthFilter
import com.orion.erp_back.security.component.provider.AuthProvider
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.builders.WebSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer
import org.springframework.security.config.annotation.web.configurers.ExceptionHandlingConfigurer
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true)
class SecurityConfig(
    private val authProvider: AuthProvider,
    private val customAuthenticationEntryPoint: CustomAuthenticationEntryPoint,
) {

    @Bean
    fun webSecurityCustomizer(): WebSecurityCustomizer {
        return WebSecurityCustomizer { webSecurity: WebSecurity ->
            webSecurity.ignoring().requestMatchers(*authProvider.ignoreListDefaultEndpoints())
        }
    }

    @Bean
    @Throws(Exception::class)
    fun authenticationManager(
        authenticationConfiguration: AuthenticationConfiguration
    ): AuthenticationManager {
        return authenticationConfiguration.authenticationManager
    }

    @Bean
    @Throws(Exception::class)
    fun filterChain(httpSecurity: HttpSecurity): SecurityFilterChain {
        return authProvider
            .defaultSecurityFilterChain(httpSecurity)
            .addFilterAfter(
                APIKeyAuthFilter(authProvider), UsernamePasswordAuthenticationFilter::class.java
            )
            .exceptionHandling { exceptionHandling: ExceptionHandlingConfigurer<HttpSecurity?> ->
                exceptionHandling.authenticationEntryPoint(
                    customAuthenticationEntryPoint
                )
            }.build()
    }
}