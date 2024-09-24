package com.orion.erp_back.user.exception

import com.orion.erp_back.common.exception.UnAuthorizedException

class UserUnAuthorizedException : UnAuthorizedException {
    constructor(userId: Long) : super(
        "Unauthorized User userId = $userId"
    )

    constructor(email: String) : super(
        "Unauthorized User email = $email"
    )
}
