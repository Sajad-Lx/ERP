package com.orion.erp_back.security.component.filter

import com.orion.erp_back.security.component.provider.AuthProvider
import com.orion.erp_back.security.exception.APIKeyNotFoundException
import com.orion.erp_back.utils.SecurityUtils
import io.micrometer.common.lang.NonNull
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.web.filter.OncePerRequestFilter

class APIKeyAuthFilter(
    private val authProvider: AuthProvider
): OncePerRequestFilter() {
    override fun doFilterInternal(
        @NonNull request: HttpServletRequest,
        @NonNull response: HttpServletResponse,
        @NonNull filterChain: FilterChain
    ) {
        try {
            authProvider.generateRequestApiKey(request)?.let {
                if (!authProvider.validateApiKey(it)) {
                    throw APIKeyNotFoundException(request.requestURI)
                }
            } ?: throw APIKeyNotFoundException(request.requestURI)

            filterChain.doFilter(request, response)
        } catch (exception: APIKeyNotFoundException) {
            SecurityUtils.sendErrorResponse(
                request,
                response,
                exception,
            )
        }
    }
}