package com.orion.erp_back.auth.dto.serve.response

data class RefreshAccessTokenResponse(
    val accessToken: String
) {
    companion object {
        fun of(accessToken: String): RefreshAccessTokenResponse =
            RefreshAccessTokenResponse(accessToken)
    }
}