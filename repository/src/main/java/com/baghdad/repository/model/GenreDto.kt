package com.baghdad.repository.model

import com.baghdad.entity.media.Genre

data class GenreDto(
    val id: Long,
    val name: String
)

fun GenreDto.toEntity(): Genre {
    return Genre(
        id = this.id,
        name = this.name
    )
}