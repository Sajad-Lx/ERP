package com.orion.erp_back.security.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.CorsConfigurationSource
import org.springframework.web.cors.UrlBasedCorsConfigurationSource

@Configuration
class CorsConfig {
    @Bean
    fun corsConfigurationSource(): CorsConfigurationSource {
        val configuration = CorsConfiguration()
        val source = UrlBasedCorsConfigurationSource()

        configuration.allowedOrigins = listOf("*")  // Allows from any origin.
        configuration.allowedMethods = listOf("GET", "POST", "PUT", "DELETE", "OPTIONS", "HEAD")
        configuration.allowedHeaders = listOf(
            "Authorization",
            "Content-Type",
            "X-Requested-With",
            "Access-Control-Allow-Origin",
            "Access-Control-Allow-Credentials",
            "Access-Control-Request-Method",
            "Access-Control-Request-Headers",
        )
        configuration.exposedHeaders = listOf(
            "Authorization",
            "Access-Control-Allow-Origin",
            "Access-Control-Allow-Credentials",
            "Content-Disposition",
        )
        configuration.allowCredentials = true
        configuration.maxAge =
            3600  // Specifies how long the results of a preflight request can be cached.
        source.registerCorsConfiguration(
            "/**",
            configuration
        )  // Applies CORS settings to all paths.
        return source
    }
}