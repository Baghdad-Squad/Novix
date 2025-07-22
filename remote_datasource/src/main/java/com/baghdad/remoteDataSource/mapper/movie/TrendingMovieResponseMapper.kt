package com.baghdad.remoteDataSource.mapper.movie

import com.baghdad.remoteDataSource.response.movie.TrendingMovieResponse
import com.baghdad.repository.model.GenreDto
import com.baghdad.repository.model.MovieDto

fun TrendingMovieResponse.Result.toDto(): MovieDto {
    return MovieDto(
        id = id ?: 0L,
        title = title ?: "Untitled",
        posterPictureURL = posterPath?.let { "https://image.tmdb.org/t/p/w500$it" } ?: "",
        trailerURL = backdropPath?.let { "https://image.tmdb.org/t/p/w500$it" } ?: "",
        overview = overview ?: "No overview available.",
        releaseDate = releaseDate ?: "0001-01-01",
        imdbRating = voteAverage ?: 0.0,
        runtimeMinutes = 0,
        userRating = popularity ?: 0.0,
        genres = genreIds?.map {
            GenreDto(
                id = it.toLong(),
                name = "",
                type = GenreDto.GenreType.MOVIE
            )
        } ?: emptyList()
    )
}
