package com.orion.erp_back.security.component.filter

import com.orion.erp_back.security.component.provider.JWTProvider
import com.orion.erp_back.utils.SecurityUtils
import io.jsonwebtoken.ExpiredJwtException
import io.micrometer.common.lang.NonNull
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.filter.OncePerRequestFilter

class JWTAuthFilter(
    private val jwtProvider: JWTProvider
) : OncePerRequestFilter() {
    override fun doFilterInternal(
        @NonNull request: HttpServletRequest,
        @NonNull response: HttpServletResponse,
        @NonNull filterChain: FilterChain
    ) {
        try {
            jwtProvider.generateRequestToken(request)?.let {
                if (jwtProvider.validateToken(it)) {
                    val usernamePasswordAuthenticationToken: UsernamePasswordAuthenticationToken =
                        jwtProvider.getAuthentication(it)

                    SecurityContextHolder.getContext().authentication =
                        usernamePasswordAuthenticationToken
                } else {
                    SecurityContextHolder.clearContext()
                }
            } ?: SecurityContextHolder.clearContext()

            filterChain.doFilter(request, response)
        } catch (exception: Exception) {
            SecurityContextHolder.clearContext()
            var message = "Invalid Token"

            if (exception is ExpiredJwtException) {
                message = "Expired Token"
            }

            SecurityUtils.sendErrorResponse(
                request,
                response,
                exception,
                message,
            )
        }
    }
}