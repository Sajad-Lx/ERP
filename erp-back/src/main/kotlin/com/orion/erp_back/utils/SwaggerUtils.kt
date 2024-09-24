package com.orion.erp_back.utils

import com.orion.erp_back.common.exception.CustomRuntimeException
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

@Component
class SwaggerUtils {
  @Value("\${springdoc.api-docs.path}")
  private val apiDocsPath: String? = null

  fun confirmPathEqualsSwaggerConfig(path: String): Boolean {
    if (apiDocsPath == null) {
      throw CustomRuntimeException("apiDocsPath is null")
    }

    val swaggerConfigUrl = "$apiDocsPath/swagger-config"
    return path == swaggerConfigUrl
  }
}
