package com.baghdad.remoteDataSource.response.tvShow

import com.baghdad.remoteDataSource.response.actor.ImageResponse
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TVShowImagesResponse(
    @SerialName("id")
    val id: Int? = null,
    @SerialName("backdrops")
    val backdrops: List<ImageResponse>? = null,
    @SerialName("logos")
    val logos: List<ImageResponse>? = null,
    @SerialName("posters")
    val posters: List<ImageResponse>? = null
)
