package com.baghdad.remoteDataSource.response.savedList

import com.google.gson.annotations.SerializedName

data class AddListItemResponse(
    @SerializedName("status_code") val statusCode: Int,
    @SerializedName("status_message") val statusMessage: String
)