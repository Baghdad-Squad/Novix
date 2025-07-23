package com.baghdad.remoteDataSource.response.movie

import com.baghdad.remoteDataSource.response.actor.ImageResponse
import com.google.gson.annotations.SerializedName

data class MovieImageResponse(
    @SerializedName("id")
    val id: Int = 0,
    @SerializedName("backdrops")
    val backdrops: List<ImageResponse>? = null,
)