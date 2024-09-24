package com.orion.erp_back.auth.dto.serve.response

import io.swagger.v3.oas.annotations.media.Schema

data class RefreshAccessTokenResponse(
    @field:Schema(
        description = "User AccessToken",
        nullable = false
    )
    val accessToken: String
) {
    companion object {
        fun of(accessToken: String): RefreshAccessTokenResponse =
            RefreshAccessTokenResponse(accessToken)
    }
}