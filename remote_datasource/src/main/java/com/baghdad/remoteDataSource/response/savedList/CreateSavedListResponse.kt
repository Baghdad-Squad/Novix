package com.baghdad.remoteDataSource.response.savedList

import com.google.gson.annotations.SerializedName

data class CreateSavedListResponse(
    @SerializedName("list_id") val listId: Int? = null,
    @SerializedName("status_message") val statusMessage: String? = null,
    @SerializedName("status_code") val statusCode: Int? = null,
    @SerializedName("success") val success: Boolean? = null
)
