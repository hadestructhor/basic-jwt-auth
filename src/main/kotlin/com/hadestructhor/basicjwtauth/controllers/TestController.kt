package com.hadestructhor.basicjwtauth.controllers

import com.hadestructhor.basicjwtauth.config.Router
import com.hadestructhor.basicjwtauth.security.jwt.JwtUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RestController


@RestController
class TestController {
    @Autowired private lateinit var jwtUtils: JwtUtils

    @GetMapping(Router.USER)
    @PreAuthorize("hasRole('USER') or hasRole('MOD') or hasRole('ADMIN')")
    fun userAccess(@RequestHeader(name="jwt-access-token") token: String): String? {
        val username = jwtUtils.getUserNameFromJwtToken(token)
        return "User Content. My username is $username"
    }

    @GetMapping(Router.MODS)
    @PreAuthorize("hasRole('MOD')")
    fun moderatorAccess(@RequestHeader(name="jwt-access-token") token: String): String? {
        val username = jwtUtils.getUserNameFromJwtToken(token)
        return "Moderator Board. My username is $username"
    }

    @GetMapping(Router.ADMINS)
    @PreAuthorize("hasRole('ADMIN')")
    fun adminAccess(@RequestHeader(name="jwt-access-token") token: String): String? {
        val username = jwtUtils.getUserNameFromJwtToken(token)
        return "Admin Board. My username is $username"
    }
}