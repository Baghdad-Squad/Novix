package com.baghdad.remoteDataSource.response.movie

import com.baghdad.remoteDataSource.response.actor.ImageResponse
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MovieImageResponse(
    @SerialName("id")
    val id: Int = 0,
    @SerialName("backdrops")
    val backdrops: List<ImageResponse>? = null,
)