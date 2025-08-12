package com.baghdad.remoteDataSource.mapper.genre

import com.baghdad.remoteDataSource.response.genre.GenreListResponse
import com.baghdad.repository.model.GenreDto

fun GenreListResponse.toDto(genreType: GenreDto.GenreType): List<GenreDto> {
    return genres.map { genre ->
        GenreDto(
            id = genre.id ?: -1L,
            name = genre.name,
            type = genreType
        )
    }
}

