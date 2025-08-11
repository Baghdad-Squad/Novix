package com.baghdad.remoteDataSource.response.movie

import com.google.gson.annotations.SerializedName
data class MovieVideosResponse(
    @SerializedName("id") val id: Long? = null,
    @SerializedName("results") val results: List<Result?>? = null,
) {
    data class Result(
        @SerializedName("id") val id: Long? = null,
        @SerializedName("key") val key: String? = null,
        @SerializedName("name") val name: String? = null,
        @SerializedName("official") val official: Boolean? = null,
        @SerializedName("published_at") val publishedAt: String? = null,
        @SerializedName("site") val site: String? = null,
        @SerializedName("size") val size: Int? = null,
        @SerializedName("type") val type: String? = null,
    )
}