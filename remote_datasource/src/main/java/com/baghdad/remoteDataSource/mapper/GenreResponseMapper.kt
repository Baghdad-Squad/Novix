package com.baghdad.remoteDataSource.util.mapper

import com.baghdad.remoteDataSource.util.entity.GenreListResponse
import com.baghdad.repository.model.GenreDto

fun GenreListResponse.toDto(genreType: GenreDto.GenreType): List<GenreDto> {
    return genres.map { genre ->
        GenreDto(
            id = genre.id.toLong(),
            name = genre.name,
            type = genreType
        )
    }
}