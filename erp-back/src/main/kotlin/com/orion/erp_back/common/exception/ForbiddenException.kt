package com.orion.erp_back.common.exception

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(HttpStatus.FORBIDDEN)
open class ForbiddenException(message: String) : CustomRuntimeException(message)