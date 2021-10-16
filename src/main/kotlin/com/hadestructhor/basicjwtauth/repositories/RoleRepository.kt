package com.hadestructhor.basicjwtauth.repositories

import com.hadestructhor.basicjwtauth.models.EnumRole
import com.hadestructhor.basicjwtauth.models.Role
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface RoleRepository : JpaRepository<Role, Long> {
    fun findRoleByName(name: EnumRole): Optional<Role>
}