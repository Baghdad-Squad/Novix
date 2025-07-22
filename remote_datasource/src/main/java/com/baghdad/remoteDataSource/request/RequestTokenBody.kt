package com.baghdad.remoteDataSource.request


data class RequestTokenBody(
    @SerializedName("request_token") val requestToken: String
)