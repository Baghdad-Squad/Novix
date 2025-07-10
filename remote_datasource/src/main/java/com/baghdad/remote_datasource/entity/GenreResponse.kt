package com.baghdad.remote_datasource.entity

import com.baghdad.repository.model.GenreDto
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
fun GenreListResponse.toDto(): List<GenreDto> {
    return genres.map { genre ->
        GenreDto(
            id = genre.id.toLong(),
            name = genre.name
        )
    }
}
