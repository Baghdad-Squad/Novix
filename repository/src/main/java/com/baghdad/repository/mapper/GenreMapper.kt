package com.baghdad.repository.mapper

import com.baghdad.entity.media.Genre
import com.baghdad.repository.model.GenreDto

fun GenreDto.toEntity(): Genre {
    return Genre(id = id, name = name)
}

/**
 * TODO learn who to pronounce it
 */
fun List<GenreDto>.toEntities(): List<Genre> {
    return map { it.toEntity() }
}