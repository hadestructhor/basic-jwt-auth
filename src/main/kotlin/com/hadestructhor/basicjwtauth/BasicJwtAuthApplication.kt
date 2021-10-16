package com.hadestructhor.basicjwtauth

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import java.security.SecureRandom

@SpringBootApplication
class BasicJwtAuthApplication

fun main(args: Array<String>) {
	runApplication<BasicJwtAuthApplication>(*args)
}
