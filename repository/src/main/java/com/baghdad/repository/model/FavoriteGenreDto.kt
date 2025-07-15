package com.baghdad.repository.model

import com.baghdad.entity.media.Genre

class FavoriteGenreDto(
    val genreId: Long,
    val name: String,
    val count: Int,
    val timeStamp: Long
)

fun FavoriteGenreDto.toEntity(): Genre {
    return Genre(id = genreId, name = name)
}