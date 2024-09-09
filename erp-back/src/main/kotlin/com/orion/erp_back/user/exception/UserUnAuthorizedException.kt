package com.orion.erp_back.user.exception

import com.orion.erp_back.common.exception.UnAuthorizedException

class UserUnAuthorizedException : UnAuthorizedException {
    constructor(userId: Long) : super(
        "User Not Found userId = $userId"
    )

    constructor(email: String) : super(
        "User Not Found email = $email"
    )
}
