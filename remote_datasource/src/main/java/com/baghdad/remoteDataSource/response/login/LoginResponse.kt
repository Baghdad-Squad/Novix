package com.baghdad.remoteDataSource.response.login

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LoginResponse (
    @SerialName("status_code")
    val statusCode : Int?=null,
    @SerialName("status_message")
    val statusMessage : String?=null,
    @SerialName("success")
    val success : Boolean?=null,
    @SerialName("session_id")
    val sessionId : String? = null
)
