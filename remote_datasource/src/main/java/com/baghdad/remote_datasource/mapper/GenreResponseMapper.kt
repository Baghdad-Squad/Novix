package com.baghdad.remote_datasource.mapper

import com.baghdad.remote_datasource.entity.GenreListResponse
import com.baghdad.repository.model.GenreDto

fun GenreListResponse.toDto(): List<GenreDto> {
    return genres.map { genre ->
        GenreDto(
            id = genre.id.toLong(),
            name = genre.name
        )
    }
}