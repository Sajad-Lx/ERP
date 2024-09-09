package com.orion.erp_back.security.exception

import com.orion.erp_back.common.exception.UnAuthorizedException

class RefreshTokenNotFoundException : UnAuthorizedException {
    constructor() : super("Refresh Token Not Found")

    constructor(userId: Long?) : super(
        "Refresh Token Not Found userId = $userId"
    )

    constructor(email: String?) : super(
        "Refresh Token Not Found email = $email"
    )
}