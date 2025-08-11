package com.baghdad.remoteDataSource.mapper.movie

import com.baghdad.remoteDataSource.response.movie.PopularMoviesResponse
import com.baghdad.repository.model.GenreDto
import com.baghdad.repository.model.MovieDto

fun PopularMoviesResponse.toMovieDtos(): List<MovieDto> = results?.mapNotNull { it?.takeIf { it.id != null }?.toDto() } ?: emptyList()

fun PopularMoviesResponse.Result.toDto(): MovieDto =
    MovieDto(
        id = id ?: 0,
        title = title ?: "",
        genres =
            genreIds?.map {
                GenreDto(
                    id = it ?: 0L,
                    name = "",
                    type = GenreDto.GenreType.MOVIE,
                )
            } ?: emptyList(),
        imdbRating = voteAverage ?: 0.0,
        userRating = 0.0,
        releaseDate = releaseDate.takeIf { !it.isNullOrEmpty() } ?: "0001-01-01",
        overview = overview ?: "",
        posterPictureURL = posterPath?.let { "https://image.tmdb.org/t/p/w500$it" } ?: "",
        trailerURL = "",
        runtimeMinutes = 0,
    )
