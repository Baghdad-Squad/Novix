package com.baghdad.remoteDataSource.mapper.movie

import com.baghdad.remoteDataSource.response.MovieResult
import com.baghdad.repository.model.MovieDto

fun MovieResult.toDto(): MovieDto {
    return MovieDto(
        id = (id ?: 0).toLong(),
        title = title ?: "Untitled",
        genres =  emptyList(),
        imdbRating = voteAverage ?: 0.0,
        userRating = 0.0,
        releaseDate = releaseDate ?: "Unknown",
        overview = overview ?: "No overview available.",
        posterPictureURL = posterPath?.let { "https://image.tmdb.org/t/p/w500$it" } ?: "",
        runtimeMinutes = 0
    )
}
