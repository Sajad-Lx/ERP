package com.orion.erp_back.common.security

import com.orion.erp_back.security.SecurityUserItem
import com.orion.erp_back.security.UserAdapter
import com.orion.erp_back.user.constant.UserRole
import com.orion.erp_back.user.entity.User
import org.instancio.Instancio
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContext
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.test.context.support.WithSecurityContextFactory

class WithMockCustomUserSecurityContextFactory
    : WithSecurityContextFactory<WithMockCustomUser> {

    override fun createSecurityContext(annotation: WithMockCustomUser): SecurityContext {
        val securityContext = SecurityContextHolder.createEmptyContext()
        val user = Instancio.create(User::class.java)
        val securityUserItem = SecurityUserItem.of(user.also {
            it.id = annotation.id.toLong()
            it.email = annotation.email
            it.username = annotation.username
            it.role = UserRole.valueOf(annotation.role)
        })

        val userAdapter = UserAdapter(securityUserItem)

        val usernamePasswordAuthenticationToken = UsernamePasswordAuthenticationToken(
            userAdapter,
            null,
            userAdapter.authorities
        )

        securityContext.authentication = usernamePasswordAuthenticationToken
        return securityContext
    }
}
