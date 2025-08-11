package com.baghdad.remoteDataSource.mapper.movie

import com.baghdad.remoteDataSource.response.movie.MovieDetailsResponse
import com.baghdad.repository.model.GenreDto
import com.baghdad.repository.model.MovieDto

fun MovieDetailsResponse.toDto(userRating: Double? = null): MovieDto {
    return MovieDto(
        id = (id ?: 0),
        title = title ?: "Untitled",
        genres = genres?.mapNotNull { it.toGenreDto() } ?: emptyList(),
        imdbRating = voteAverage ?: 0.0,
        userRating = userRating,
        releaseDate = releaseDate ?: "Unknown",
        overview = overview ?: "No overview available.",
        posterPictureURL = posterPath?.let { "https://image.tmdb.org/t/p/w500$it" } ?: "",
        runtimeMinutes = runtime ?: 0,
        trailerURL = ""
    )
}

fun MovieDetailsResponse.Genre.toGenreDto(): GenreDto? {
    return if (id != null && !name.isNullOrBlank()) {
        GenreDto(
            id = id, name = name,
            type = GenreDto.GenreType.MOVIE
        )
    } else null
}

