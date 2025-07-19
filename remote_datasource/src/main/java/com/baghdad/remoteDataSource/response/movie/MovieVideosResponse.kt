package com.baghdad.remoteDataSource.response.movie

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MovieVideosResponse(
    @SerialName("id") val id: Int? = null,
    @SerialName("results") val results: List<Result?>? = null
) {
    @Serializable
    data class Result(
        @SerialName("id") val id: String? = null,
        @SerialName("key") val key: String? = null,
        @SerialName("name") val name: String? = null,
        @SerialName("official") val official: Boolean? = null,
        @SerialName("published_at") val publishedAt: String? = null,
        @SerialName("site") val site: String? = null,
        @SerialName("size") val size: Int? = null,
        @SerialName("type") val type: String? = null
    )
}