package com.hadestructhor.basicjwtauth.controllers.payloads

data class TokenRefreshResponse (
        val accessToken: String,
        val refreshToken: String
)
