package com.baghdad.remoteDataSource.request

import com.google.gson.annotations.SerializedName

data class CreateListRequest(
    @SerializedName("name") val name: String,
    @SerializedName("description") val description: String? = null,
    @SerializedName("language") val language: String? = null,
)
