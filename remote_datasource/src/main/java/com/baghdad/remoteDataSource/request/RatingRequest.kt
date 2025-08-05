package com.baghdad.remoteDataSource.request

import com.google.gson.annotations.SerializedName

data class RatingRequest(
    @SerializedName("value") val rating: Int
)
