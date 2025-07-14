package com.baghdad.remoteDataSource.mapper

import com.baghdad.repository.model.GenreDto

internal fun mapGenreIdsToGenres(
    genreIds: List<Int>,
    availableGenres: List<GenreDto>?
): List<GenreDto> {
    if (genreIds.isEmpty() || availableGenres?.isEmpty() == true) return emptyList()

    return genreIds.mapNotNull { genreId ->
        availableGenres?.firstOrNull{it.id == genreId.toLong()}
    }
}