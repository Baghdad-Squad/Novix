package com.baghdad.remoteDataSource.response.tvShow

import com.baghdad.remoteDataSource.response.actor.ImageResponse
import com.google.gson.annotations.SerializedName

data class TVShowImagesResponse(
    @SerializedName("id")
    val id: Int? = null,
    @SerializedName("backdrops")
    val backdrops: List<ImageResponse>? = null,
    @SerializedName("logos")
    val logos: List<ImageResponse>? = null,
    @SerializedName("posters")
    val posters: List<ImageResponse>? = null
)
