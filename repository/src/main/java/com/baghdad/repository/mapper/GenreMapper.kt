package com.baghdad.repository.mapper

import com.baghdad.entity.media.Genre
import com.baghdad.repository.model.GenreDto

fun GenreDto.toEntity(): Genre {
    return Genre(id = id, name = name)
}

fun List<GenreDto>.toEntities(): List<Genre> {
    return map { it.toEntity() }
}