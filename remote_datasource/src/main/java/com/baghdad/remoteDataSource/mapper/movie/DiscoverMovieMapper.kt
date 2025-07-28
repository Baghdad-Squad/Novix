package com.baghdad.remoteDataSource.mapper.movie

import com.baghdad.remoteDataSource.response.movie.DiscoverMovieResponse
import com.baghdad.repository.model.MovieDto

fun DiscoverMovieResponse.toMovieDtos() =
    results?.mapNotNull {
        it?.toDto(
            genreIds = it.genreIds,
        )
    } ?: emptyList()

fun DiscoverMovieResponse.Result.toDto(genreIds: List<Long?>? = null): MovieDto =
    MovieDto(
        id = id ?: 0L,
        title = title ?: "Untitled",
        genres =
            genreIds?.map {
                com.baghdad.repository.model.GenreDto(
                    it?.toLong() ?: 0L,
                    "",
                    com.baghdad.repository.model.GenreDto.GenreType.MOVIE,
                )
            } ?: emptyList(),
        imdbRating = voteAverage ?: 0.0,
        userRating = 0.0,
        releaseDate = releaseDate.takeIf { !it.isNullOrEmpty() } ?: "0001-01-01",
        overview = overview ?: "",
        posterPictureURL = posterPath?.let { "https://image.tmdb.org/t/p/w500$it" } ?: "",
        runtimeMinutes = 0,
        trailerURL = "",
    )
