package com.baghdad.remoteDataSource.request

import com.google.gson.annotations.SerializedName

data class CreateListRequest(
    @SerializedName("name") val name: String
)
