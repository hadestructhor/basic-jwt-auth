package com.hadestructhor.basicjwtauth.controllers.payloads

data class SignupRequest(
        val username: String,
        val email: String?,
        val password: String
)
