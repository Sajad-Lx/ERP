package com.orion.erp_back.security.exception

import com.orion.erp_back.common.exception.UnAuthorizedException

class APIKeyNotFoundException(requestURI: String?) : UnAuthorizedException(
    "API Key Not Found requestURI = $requestURI"
)