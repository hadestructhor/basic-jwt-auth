package com.hadestructhor.basicjwtauth.controllers

import com.hadestructhor.basicjwtauth.config.Router
import com.hadestructhor.basicjwtauth.controllers.payloads.JwtResponse
import com.hadestructhor.basicjwtauth.controllers.payloads.LoginRequest
import com.hadestructhor.basicjwtauth.controllers.payloads.MessageResponse
import com.hadestructhor.basicjwtauth.controllers.payloads.SignupRequest
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
class AuthController(
        @Autowired val authenticationManager: AuthenticationManager,
        @Autowired val userRepository: UserRepository,
        @Autowired val roleRepository: RoleRepository,
        @Autowired val encoder: PasswordEncoder,
        @Autowired val jwtUtils: JwtUtils,
        ) {

    @PostMapping(Router.SIGNIN)
    fun signin(@RequestBody loginRequest: LoginRequest): ResponseEntity<*> {
        try {
            val authentication: Authentication = authenticationManager.authenticate(
                    UsernamePasswordAuthenticationToken(loginRequest.username, loginRequest.password))
            SecurityContextHolder.getContext().authentication = authentication
            val jwt = jwtUtils.generateJwtToken(authentication)

            val userDetails: UserDetailsImplementation = authentication.principal as UserDetailsImplementation
            val user = userRepository.getById(userDetails.id)
            return ResponseEntity.ok(
                    JwtResponse(jwt,
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
        val jwt = jwtUtils.generateJwtToken(authentication)

        return ResponseEntity.ok(
                JwtResponse(jwt,
                        user.id,
                        user.username,
                        user.email,
                        user.roles)
        )
    }

}