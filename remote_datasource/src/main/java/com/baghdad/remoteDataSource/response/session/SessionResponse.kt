package com.baghdad.remoteDataSource.response.session

import com.google.gson.annotations.SerializedName
data class SessionResponse(
    @SerializedName("session_id") val sessionId: String,
    @SerializedName("success") val success: Boolean
)