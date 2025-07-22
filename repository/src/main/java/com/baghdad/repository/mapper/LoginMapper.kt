package com.baghdad.repository.mapper

import com.baghdad.entity.login.Login
import com.baghdad.repository.model.LoginDto

fun LoginDto.toEntity(): Login {
    return Login(
        userName = this.userName,
        userId = this.userId,
        requestToken = this.requestToken,
        password = this.password,
        statusCode = this.statusCode,
        statusMessage = this.statusMessage,
        success = this.success,
        sessionId = this.sessionId
    )
}