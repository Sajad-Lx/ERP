package com.orion.erp_back.common.security

import com.orion.erp_back.common.BaseIntegrationControllerItem
import com.orion.erp_back.security.component.provider.JWTProvider
import org.springframework.boot.test.mock.mockito.MockBean

open class SecurityItem : BaseIntegrationControllerItem() {
    @MockBean
    protected lateinit var jwtProvider: JWTProvider
}