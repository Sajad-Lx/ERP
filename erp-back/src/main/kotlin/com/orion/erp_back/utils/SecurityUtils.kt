package com.orion.erp_back.utils

import com.fasterxml.jackson.databind.ObjectMapper
import com.orion.erp_back.common.response.ErrorResponse
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import java.io.IOException

object SecurityUtils {
    fun sendErrorResponse(
        request: HttpServletRequest,
        response: HttpServletResponse,
        exception: Exception,
        message: String = "",
    ) {
        val objectMapper = ObjectMapper()

        val errorResponse = ErrorResponse.of(
            HttpStatus.UNAUTHORIZED.value(),
            message,
            exception.message ?: "Security Filter Error"
        )

        with(response) {
            status = HttpStatus.UNAUTHORIZED.value()
            contentType = MediaType.APPLICATION_JSON_VALUE

            try {
                writer.write(objectMapper.writeValueAsString(errorResponse))
            } catch (ioException: IOException) {
                ioException.printStackTrace()
            }
        }
    }
}