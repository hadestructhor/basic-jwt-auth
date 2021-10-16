package com.hadestructhor.basicjwtauth.repositories

import com.hadestructhor.basicjwtauth.models.EnumRole
import com.hadestructhor.basicjwtauth.models.Role
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface RoleRepository : JpaRepository<Role, Long> {
    fun findRoleByName(name: EnumRole): Optional<Role>
}