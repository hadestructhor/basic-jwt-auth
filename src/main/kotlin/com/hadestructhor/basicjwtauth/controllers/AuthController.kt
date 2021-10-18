package com.hadestructhor.basicjwtauth.controllers

import com.hadestructhor.basicjwtauth.config.Router
import com.hadestructhor.basicjwtauth.controllers.payloads.*
import com.hadestructhor.basicjwtauth.models.EnumRole
import com.hadestructhor.basicjwtauth.models.User
import com.hadestructhor.basicjwtauth.repositories.RoleRepository
import com.hadestructhor.basicjwtauth.repositories.UserRepository
import com.hadestructhor.basicjwtauth.security.UserDetailsImplementation
import com.hadestructhor.basicjwtauth.security.jwt.JwtUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController


@RestController
class AuthController {

    @Autowired private lateinit var authenticationManager: AuthenticationManager
    @Autowired private lateinit var userRepository: UserRepository
    @Autowired private lateinit var roleRepository: RoleRepository
    @Autowired private lateinit var encoder: PasswordEncoder
    @Autowired private lateinit var jwtUtils: JwtUtils

    @PostMapping(Router.SIGNIN)
    fun signin(@RequestBody loginRequest: LoginRequest): ResponseEntity<*> {
        try {
            val authentication: Authentication = authenticationManager.authenticate(
                    UsernamePasswordAuthenticationToken(loginRequest.username, loginRequest.password))
            SecurityContextHolder.getContext().authentication = authentication

            val userDetails: UserDetailsImplementation = authentication.principal as UserDetailsImplementation
            val user = userRepository.getById(userDetails.id)
            val accessToken = jwtUtils.generateJwtTokenFromUser(user)
            val refreshToken = jwtUtils.generateRefreshTokenFromUser(user)

            return ResponseEntity.ok(
                    JwtResponse(
                        accessToken,
                        refreshToken,
                        user.id,
                        user.username,
                        user.email,
                        user.roles)
            )
        } catch (e: BadCredentialsException) {
            return ResponseEntity
                    .badRequest()
                    .body(MessageResponse("Error: Incorrect credentials were given !"))
        }
    }

    @PostMapping(Router.SIGNUP)
    fun registerUser(@RequestBody signUpRequest: SignupRequest): ResponseEntity<*>? {
        if (userRepository.existsByUsername(signUpRequest.username)) {
            return ResponseEntity
                    .badRequest()
                    .body(MessageResponse("Error: Username is already taken!"))
        }
        signUpRequest.email?.let {
            if(userRepository.existsByEmail(it)) return ResponseEntity
                    .badRequest()
                    .body(MessageResponse("Error: Email is already in use!"))
        }

        val userRole = roleRepository.findRoleByName(EnumRole.ROLE_USER).get()

        val user = User(
            0,
                signUpRequest.username,
                signUpRequest.email,
                encoder.encode(signUpRequest.password),
                arrayListOf(userRole)
        )
        userRepository.save<User>(user)

        val authentication: Authentication = authenticationManager.authenticate(
                UsernamePasswordAuthenticationToken(user.username, signUpRequest.password))
        SecurityContextHolder.getContext().authentication = authentication
        val accessToken = jwtUtils.generateJwtTokenFromUser(user)

        val refreshToken = jwtUtils.generateRefreshTokenFromUser(user)

        return ResponseEntity.ok(
                JwtResponse(
                        accessToken,
                        refreshToken,
                        user.id,
                        user.username,
                        user.email,
                        user.roles)
        )
    }

    @PostMapping(Router.REFRESH_TOKEN)
    fun refreshToken(@RequestBody requestRefreshToken: RefreshTokenRequest): ResponseEntity<*>? {
        try {
            if (jwtUtils.validateJwtRefreshToken(requestRefreshToken.refreshToken)) {
                val user: User = userRepository.getById(jwtUtils.getUserIdFromRefreshToken(requestRefreshToken.refreshToken))
                val accessToken = jwtUtils.generateJwtTokenFromUser(user)
                val refreshToken = jwtUtils.generateRefreshTokenFromUser(user)
                return ResponseEntity.ok(
                        JwtResponse(
                                accessToken,
                                refreshToken,
                                user.id,
                                user.username,
                                user.email,
                                user.roles)
                )
            }
        } catch (e: Exception) {
            return ResponseEntity
                    .badRequest()
                    .body(MessageResponse("Error: ${e.message}"))
        }
        return null
    }

}