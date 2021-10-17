package com.hadestructhor.basicjwtauth.repositories

import com.hadestructhor.basicjwtauth.models.RefreshToken
import com.hadestructhor.basicjwtauth.models.User
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*


interface RefreshTokenRepository : JpaRepository<RefreshToken, Long> {
    override fun findById(id: Long): Optional<RefreshToken>
    fun findByToken(token: String): Optional<RefreshToken>
    fun deleteByUser(user: User): Boolean
}