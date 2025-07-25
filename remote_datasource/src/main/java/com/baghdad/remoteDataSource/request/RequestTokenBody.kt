package com.baghdad.remoteDataSource.request

import com.google.gson.annotations.SerializedName


data class RequestTokenBody(
    @SerializedName("request_token") val requestToken: String
)