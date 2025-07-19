package com.baghdad.remoteDataSource.response.episode

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class EpisodeImageResponse(
    @SerialName("id") val id: Int? = null,
    @SerialName("stills") val stills: List<Still>? = null
) {
    @Serializable
    data class Still(
        @SerialName("file_path") val filePath: String? = null,
    )
}
