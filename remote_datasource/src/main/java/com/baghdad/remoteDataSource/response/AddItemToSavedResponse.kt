package com.baghdad.remoteDataSource.response

import com.google.gson.annotations.SerializedName

data class AddItemToSavedResponse(
    @SerializedName("status_code") val statusCode: Int,
    @SerializedName("status_message") val statusMessage: String
)
