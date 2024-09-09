package com.orion.erp_back.user.exception

import com.orion.erp_back.common.exception.NotFoundException

class RoleNotFoundException(roleName: String) : NotFoundException(
    "Role Not Found roleName = $roleName"
)