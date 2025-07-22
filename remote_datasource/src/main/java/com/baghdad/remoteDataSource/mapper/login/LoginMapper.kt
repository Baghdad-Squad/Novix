package com.baghdad.remoteDataSource.mapper.login

import com.baghdad.remoteDataSource.response.login.LoginResponse
import com.baghdad.repository.model.LoginDto
 // this class has error cuz the api just return three thing.
// i wanna ask in this point
fun LoginResponse.toDto(): LoginDto {
    return LoginDto(
        userName = this.userName,
        userId = this.userId,
        statusCode = this.statusCode,
        requestToken=this.requestToken,
        statusMessage = this.statusMessage,
        success = this.success,
        sessionId = this.sessionId
    )
}
