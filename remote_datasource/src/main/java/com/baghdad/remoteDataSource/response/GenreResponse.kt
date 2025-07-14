package com.baghdad.remoteDataSource.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GenreListResponse(
    @SerialName("genres")
    val genres: List<GenreItemDto>
)

@Serializable
data class GenreItemDto(
    @SerialName("id")
    val id: Int,
    @SerialName("name")
    val name: String
)
