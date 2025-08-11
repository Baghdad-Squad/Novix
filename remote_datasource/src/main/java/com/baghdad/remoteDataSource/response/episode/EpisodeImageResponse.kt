package com.baghdad.remoteDataSource.response.episode

import com.google.gson.annotations.SerializedName
data class EpisodeImageResponse(
    @SerializedName("id") val id: Long? = null,
    @SerializedName("stills") val episodeFrames: List<EpisodeFrame>? = null,
) {
    data class EpisodeFrame(
        @SerializedName("file_path") val filePath: String? = null,
    )
}
