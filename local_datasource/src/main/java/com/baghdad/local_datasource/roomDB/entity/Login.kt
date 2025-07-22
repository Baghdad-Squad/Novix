package com.baghdad.local_datasource.roomDB.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.baghdad.repository.model.LoginDto

@Entity(tableName = "login")
data class Login(
    @PrimaryKey val userId: Long=1L,
    val userName :String="",
    val requestToken:String,
    val password: String = "",
    val statusCode: Int = 0,
    val statusMessage: String = "",
    val success: Boolean = false,
    val sessionId: String? = null
)
fun Login.toDto():LoginDto=
    LoginDto(
        userName = this.userName,
        password = this.password,
        userId = this.userId,
        requestToken = this.requestToken,
        statusCode = this.statusCode,
        statusMessage = this.statusMessage,
        success = this.success,
        sessionId = this.sessionId.toString()
    )
fun LoginDto.toEntity(): Login =
    Login(
        userName = this.userName,
        password = this.password,
        userId = this.userId,
        requestToken = this.requestToken,
        statusCode = this.statusCode,
        statusMessage = this.statusMessage ,
        success = this.success ,
        sessionId = this.sessionId
    )