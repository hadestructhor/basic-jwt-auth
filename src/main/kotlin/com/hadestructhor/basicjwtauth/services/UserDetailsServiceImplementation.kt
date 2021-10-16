package com.hadestructhor.basicjwtauth.services

import com.hadestructhor.basicjwtauth.repositories.UserRepository
import com.hadestructhor.basicjwtauth.security.UserDetailsImplementation
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class UserDetailsServiceImplementation(@Autowired var userRepository: UserRepository) : UserDetailsService {
    @Transactional
    override fun loadUserByUsername(username: String): UserDetails {
        val user = userRepository.findByUsername(username)
                .orElseThrow { UsernameNotFoundException("User Not Found with username: $username") }

        return UserDetailsImplementation.build(user)
    }
}