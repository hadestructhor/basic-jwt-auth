package com.hadestructhor.basicjwtauth.security.jwt

import com.hadestructhor.basicjwtauth.security.UserDetailsImplementation
import io.jsonwebtoken.*
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Component
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger
import java.lang.Exception
import java.security.Key
import java.util.*


@Component
class JwtUtils {
    private val jwtSecret: Key = Keys.secretKeyFor(SignatureAlgorithm.HS512);

    @Value("\${security.jwt.expiration-time-ms}")
    private val jwtExpirationMs = 86400000

    fun generateJwtToken(authentication: Authentication): String {
        val userPrincipal: UserDetailsImplementation = authentication.principal as UserDetailsImplementation
        val date: Date = Date()
        return Jwts.builder()
                .setSubject(userPrincipal.username)
                .setIssuedAt(date)
                .setExpiration(Date(date.time + jwtExpirationMs))
                .signWith(jwtSecret)
                .compact()
    }

    fun getUserNameFromJwtToken(token: String?): String {
        return Jwts.parserBuilder().setSigningKey(jwtSecret).build().parseClaimsJws(token).body.subject
    }

    fun validateJwtToken(authToken: String?): Boolean {
        try {
            Jwts.parserBuilder().setSigningKey(jwtSecret).build().parseClaimsJws(authToken)
            return true
        } catch (e: MalformedJwtException) {
            logger.error("Invalid JWT token: {}", e.message)
        } catch (e: ExpiredJwtException) {
            logger.error("JWT token is expired: {}", e.message)
        } catch (e: UnsupportedJwtException) {
            logger.error("JWT token is unsupported: {}", e.message)
        } catch (e: IllegalArgumentException) {
            logger.error("JWT claims string is empty: {}", e.message)
        } catch (e: Exception) {
            logger.error("Error: {}", e.message)
        }
        return false
    }

    companion object {
        private val logger = LoggerFactory.getLogger(JwtUtils::class.java)
    }

}