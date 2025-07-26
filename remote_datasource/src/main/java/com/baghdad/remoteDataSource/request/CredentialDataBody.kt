package com.baghdad.remoteDataSource.request

import com.google.gson.annotations.SerializedName

data class CredentialDataBody(
    @SerializedName("password") val password: String,
    @SerializedName("request_token") val requestToken: String,
    @SerializedName("username") val userName: String
)