package com.baghdad.remoteDataSource.response.tvShow

import com.google.gson.annotations.SerializedName
data class TVShowImagesResponse(
    @SerializedName("id") val id: Long? = null,
    @SerializedName("backdrops") val backdrops: List<ImageResponse>? = null,
    @SerializedName("logos") val logos: List<ImageResponse>? = null,
    @SerializedName("posters") val posters: List<ImageResponse>? = null,
) {
    data class ImageResponse(
        @SerializedName("file_path") val filePath: String? = null,
    )
}

