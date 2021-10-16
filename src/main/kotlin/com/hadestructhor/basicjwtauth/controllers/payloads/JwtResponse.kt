package com.hadestructhor.basicjwtauth.controllers.payloads

import com.hadestructhor.basicjwtauth.models.Role

data class JwtResponse(
        var accessToken: String,
        var userId: Long,
        var username: String,
        var email: String?,
        var roles: List<Role>?
)
