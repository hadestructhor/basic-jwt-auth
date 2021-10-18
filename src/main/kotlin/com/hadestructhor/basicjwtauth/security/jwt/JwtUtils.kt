package com.hadestructhor.basicjwtauth.security.jwt

import com.hadestructhor.basicjwtauth.models.User
import com.hadestructhor.basicjwtauth.repositories.UserRepository
import io.jsonwebtoken.*
import io.jsonwebtoken.security.Keys
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.security.Key
import java.util.*


@Component
class JwtUtils {
    private val jwtAccessSecret: Key = Keys.secretKeyFor(SignatureAlgorithm.HS512)
    private val jwtRefreshSecret: Key = Keys.secretKeyFor(SignatureAlgorithm.HS512)

    @Value("\${security.jwt.expiration-time-ms}")
    private val jwtExpirationMs = 14400000

    @Value("\${security.jwt-refresh.expiration-ms}")
    private val refreshTokenDurationMs: Long = 36000000

    @Autowired
    lateinit var userRepository: UserRepository

    fun generateJwtTokenFromUser(user: User): String {
        val date: Date = Date()
        return Jwts.builder()
                .setSubject(user.username)
                .setIssuedAt(date)
                .setExpiration(Date(date.time + jwtExpirationMs))
                .claim("userId", user.id)
                .claim("email", user.email)
                .claim("roles", user.roles)
                .signWith(jwtAccessSecret)
                .compact()
    }

    fun generateRefreshTokenFromUser(user: User) : String {
        val date: Date = Date()
        return Jwts.builder()
                .setSubject(user.username)
                .setIssuedAt(date)
                .setExpiration(Date(date.time + refreshTokenDurationMs))
                .claim("userId", user.id)
                .signWith(jwtRefreshSecret)
                .compact()
    }

    fun getUserNameFromJwtToken(token: String): String {
        return Jwts.parserBuilder().setSigningKey(jwtAccessSecret).build().parseClaimsJws(token).body.subject
    }

    fun getUserIdFromRefreshToken(token: String): Long {
        return Jwts.parserBuilder().setSigningKey(jwtRefreshSecret).build().parseClaimsJws(token).body["userId"].toString().toLong()
    }

    fun validateJwtToken(jwtToken: String): Boolean {
        try {
            Jwts.parserBuilder().setSigningKey(jwtAccessSecret).build().parseClaimsJws(jwtToken)
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

    fun validateJwtRefreshToken(jwtToken: String): Boolean {
        try {
            Jwts.parserBuilder().setSigningKey(jwtRefreshSecret).build().parseClaimsJws(jwtToken)
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