package com.hadestructhor.basicjwtauth.security

import com.hadestructhor.basicjwtauth.models.User
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import java.util.*
import java.util.stream.Collectors

class UserDetailsImplementation(
        val id: Long,
        private val username: String,
        val email: String?,
        private val password: String,
        private val authorities: Collection<GrantedAuthority>
        ) : UserDetails {

    override fun getAuthorities(): Collection<GrantedAuthority> {
        return authorities
    }

    override fun getPassword(): String {
        return password
    }

    override fun getUsername(): String {
        return username
    }

    override fun isAccountNonExpired(): Boolean {
        return true
    }

    override fun isAccountNonLocked(): Boolean {
        return true
    }

    override fun isCredentialsNonExpired(): Boolean {
        return true
    }

    override fun isEnabled(): Boolean {
        return true
    }

    override fun equals(o: Any?): Boolean {
        if (this === o) return true
        if (o == null || javaClass != o.javaClass) return false
        val user = o as UserDetailsImplementation
        return Objects.equals(id, user.id)
    }

    companion object {
        private const val serialVersionUID = 1L
        fun build(user: User): UserDetailsImplementation {
            val authorities: List<GrantedAuthority> = user.roles.stream()
                    .map { role -> SimpleGrantedAuthority(role.name.name) }
                    .collect(Collectors.toList())
            return UserDetailsImplementation(
                    user.id,
                    user.username,
                    user.email,
                    user.password,
                    authorities)
        }
    }
}