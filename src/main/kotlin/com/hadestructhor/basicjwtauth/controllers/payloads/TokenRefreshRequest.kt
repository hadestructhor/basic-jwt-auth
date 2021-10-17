package com.hadestructhor.basicjwtauth.controllers.payloads

import javax.validation.constraints.NotBlank

data class TokenRefreshRequest (
        @NotBlank
        val refreshToken: String
)
