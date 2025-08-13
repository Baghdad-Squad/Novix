package com.baghdad.remoteDataSource.response.rate

import com.google.gson.annotations.SerializedName
data class RatingResponse(
    @SerializedName("success") val isSuccess: Boolean,
    @SerializedName("status_code") val statusCode: Int,
    @SerializedName("status_message") val statusMessage: String
)