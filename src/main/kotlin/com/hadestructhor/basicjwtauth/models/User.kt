package com.hadestructhor.basicjwtauth.models

import javax.persistence.*
import javax.validation.constraints.NotBlank
import javax.validation.constraints.Size

@Entity
@Table(name = "users")
data class User(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        var id: Long,

        @Column(unique=true)
        @NotBlank
        @Size(max = 20)
        var username: String,

        @Column(unique=true)
        var email: String?,

        @NotBlank
        @Size(max = 120)
        var password: String,

        @ManyToMany(fetch = FetchType.LAZY)
        @JoinTable(name = "user_roles",
                joinColumns = [JoinColumn(name = "user_id", referencedColumnName = "id")],
                inverseJoinColumns = [JoinColumn(name = "role_id", referencedColumnName = "id")])
        var roles: List<Role>
)
