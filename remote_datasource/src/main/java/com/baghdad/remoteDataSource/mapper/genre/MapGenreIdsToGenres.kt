package com.baghdad.remoteDataSource.mapper.genre

import com.baghdad.repository.model.GenreDto

fun mapGenreIdsToGenres(
    genreIds: List<Int>,
    availableGenres: List<GenreDto>?,
): List<GenreDto> {
    if (genreIds.isEmpty() || availableGenres.isNullOrEmpty()) return emptyList()

    return genreIds.mapNotNull { genreId ->
        availableGenres.find { it.id == genreId.toLong() }
    }
}
