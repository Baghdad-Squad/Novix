package com.baghdad.remoteDataSource.response.movie

import com.google.gson.annotations.SerializedName
data class MovieImageResponse(
    @SerializedName("id") val id: Long? = null,
    @SerializedName("backdrops") val backdrops: List<ImageResponse>? = null,
) {
    data class ImageResponse(
        @SerializedName("file_path") val filePath: String? = null,
    )
}