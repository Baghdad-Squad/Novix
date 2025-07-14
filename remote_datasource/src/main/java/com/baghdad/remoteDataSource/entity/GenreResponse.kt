package com.baghdad.remoteDataSource.util.entity

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GenreListResponse(
    @SerialName("genres")
    val genres: List<GenreItem>
)

@Serializable
data class GenreItem(
    @SerialName("id")
    val id: Int,
    @SerialName("name")
    val name: String
)
