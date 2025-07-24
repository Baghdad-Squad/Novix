package com.baghdad.remoteDataSource.response.episode

import com.google.gson.annotations.SerializedName

data class EpisodeImageResponse(
    @SerializedName("id") val id: Int? = null,
    @SerializedName("stills") val stills: List<Still>? = null
) {
    data class Still(
        @SerializedName("file_path") val filePath: String? = null,
    )
}
