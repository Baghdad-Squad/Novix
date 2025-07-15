package com.baghdad.remoteDataSource.mapper

import com.baghdad.remoteDataSource.response.GenreListResponse
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

