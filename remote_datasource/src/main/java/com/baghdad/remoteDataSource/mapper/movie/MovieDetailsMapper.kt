package com.baghdad.remoteDataSource.mapper.movie

import com.baghdad.remoteDataSource.response.movie.MovieDetailsResponse
import com.baghdad.remoteDataSource.util.getImageUrlFromPath
import com.baghdad.repository.model.GenreDto
import com.baghdad.repository.model.MovieDto

fun MovieDetailsResponse.toDto(userRating: Double? = null): MovieDto {
    return MovieDto(
        id = (id ?: -1L),
        title = title.orEmpty(),
        genres = genres?.mapNotNull { it.toGenreDto() }.orEmpty(),
        imdbRating = voteAverage ?: 0.0,
        userRating = userRating,
        releaseDate = releaseDate.orEmpty(),
        overview = overview.orEmpty(),
        posterPictureURL = getImageUrlFromPath(posterPath),
        runtimeMinutes = runtime ?: 0,
        trailerURL = "",
    )
}

private fun MovieDetailsResponse.Genre.toGenreDto(): GenreDto? {
    return if (id != null && !name.isNullOrBlank()) {
        GenreDto(
            id = id,
            name = name,
            type = GenreDto.GenreType.MOVIE,
        )
    } else {
        null
    }
}