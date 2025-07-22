package com.baghdad.repository.model

class LoginDto(
    val userName: String,
    val password: String,
    val userId: Long,
    val requestToken: String,
    val statusCode: Int,
    val statusMessage: String,
    val success: Boolean,
    val sessionId: String
)