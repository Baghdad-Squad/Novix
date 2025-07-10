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
fun GenreItem.toDto(): GenreDto {
    return GenreDto(
        id = id.toLong(),
        name = name
    )
}
fun GenreListResponse.toDto(): List<GenreDto> {
    return genres.map { it.toDto() }
}