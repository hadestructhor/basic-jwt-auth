package com.hadestructhor.basicjwtauth.services

import com.hadestructhor.basicjwtauth.exception.TokenRefreshException
import com.hadestructhor.basicjwtauth.models.RefreshToken
import com.hadestructhor.basicjwtauth.repositories.RefreshTokenRepository
import com.hadestructhor.basicjwtauth.repositories.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.Instant
import java.util.*


@Service
class RefreshTokenService {
    @Value("\${security.jwt-refresh.expiration-ms}")
    private val refreshTokenDurationMs: Long = 86400000

    @Autowired
    private lateinit var refreshTokenRepository: RefreshTokenRepository

    @Autowired
    private lateinit var userRepository: UserRepository

    fun findByToken(token: String): Optional<RefreshToken> {
        return refreshTokenRepository.findByToken(token)
    }

    fun createRefreshToken(userId: Long): RefreshToken {
        var refreshToken = RefreshToken()
        refreshToken.user = userRepository.findById(userId).get()
        refreshToken.expiryDate = Instant.now().plusMillis(refreshTokenDurationMs)
        refreshToken.token = UUID.randomUUID().toString()
        refreshToken = refreshTokenRepository.save(refreshToken)
        return refreshToken
    }

    fun verifyExpiration(token: RefreshToken): RefreshToken {
        if (token.expiryDate!!.compareTo(Instant.now()) < 0) {
            refreshTokenRepository.delete(token)
            throw TokenRefreshException(token.token!!, "Refresh token was expired. Please make a new signin request")
        }
        return token
    }

    @Transactional
    fun deleteByUserId(userId: Long): Boolean {
        return refreshTokenRepository.deleteByUser(userRepository.findById(userId).get())
    }
}