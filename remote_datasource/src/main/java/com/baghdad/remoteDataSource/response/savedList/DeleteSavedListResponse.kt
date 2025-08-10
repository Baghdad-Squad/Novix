package com.baghdad.remoteDataSource.response.savedList

import com.google.gson.annotations.SerializedName

data class DeleteSavedListResponse(
    @SerializedName("status_code") val statusCode: Int? = null,
    @SerializedName("status_message") val statusMessage: String? = null
)
