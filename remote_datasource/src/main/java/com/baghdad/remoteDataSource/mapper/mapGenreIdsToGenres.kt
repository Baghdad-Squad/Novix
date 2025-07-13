package com.baghdad.remoteDataSource.util.mapper

import com.baghdad.repository.model.GenreDto

internal fun mapGenreIdsToGenres(
    genreIds: List<Int>,
    availableGenres: List<GenreDto>?
): List<GenreDto> {
    if (genreIds.isEmpty() || availableGenres?.isEmpty() == true) return emptyList()

    val genreMap = availableGenres?.associateBy { it.id.toInt() }
    return genreIds.mapNotNull { genreId ->
        genreMap?.get(genreId)
    }
}