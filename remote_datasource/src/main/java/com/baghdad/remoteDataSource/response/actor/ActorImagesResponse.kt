package com.baghdad.remoteDataSource.response.actor

import com.google.gson.annotations.SerializedName
data class ActorImagesResponse(
    @SerializedName("id") val id: Long? = null,
    @SerializedName("profiles") val profiles: List<ImageResponse>? = null,
) {
    data class ImageResponse(
        @SerializedName("file_path") val filePath: String? = null,
    )
}