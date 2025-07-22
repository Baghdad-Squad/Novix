package com.baghdad.entity.login

data class Login(
    val userName: String,
    val userId: Long,
    val requestToken: String,
    val password: String,
    val statusCode: Int,
    val statusMessage: String,
    val success: Boolean,
    val sessionId: String? = null
)
