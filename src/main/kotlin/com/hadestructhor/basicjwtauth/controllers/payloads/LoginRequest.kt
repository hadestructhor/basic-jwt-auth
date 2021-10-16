package com.hadestructhor.basicjwtauth.controllers.payloads

data class LoginRequest(
        var username: String,
        var password: String
)