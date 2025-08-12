package com.baghdad.remoteDataSource.response.token

import com.google.gson.annotations.SerializedName
data class RequestTokenResponse(
    @SerializedName("expires_at") val expireAt: String,
    @SerializedName("request_token") val requestToken: String,
    val success: Boolean,
)