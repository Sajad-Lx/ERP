package com.orion.erp_back.common.security

import org.springframework.security.test.context.support.WithSecurityContext

@Retention(AnnotationRetention.RUNTIME)
@WithSecurityContext(factory = WithMockCustomUserSecurityContextFactory::class)
annotation class WithMockCustomUser(
    val id: String = "1",
    val email: String = "myemail@gmail.com",
    val username: String = "OrionXD",
    val role: String = "STANDARD_USER"
)