package com.hadestructhor.basicjwtauth.advice

import java.util.*

data class ErrorMessage(
        val statusCode: Int,
        val timestamp: Date,
        val message: String,
        val description: String
        )