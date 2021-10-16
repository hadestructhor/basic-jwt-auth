package com.hadestructhor.basicjwtauth.models

import javax.persistence.*

@Entity
@Table(name = "roles")
data class Role(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        var id: Long,

        @Enumerated(EnumType.STRING)
        @Column(length = 20)
        var name: EnumRole
)
