package com.baghdad.remoteDataSource.response.actor

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ActorImagesResponse(
    @SerialName("id")
    val id: Int? = null,
    @SerialName("profiles")
    val profiles: List<ActorImageResponse>? = null
)

@Serializable
data class ActorImageResponse(
    @SerialName("file_path")
    val filePath: String? = null,
)