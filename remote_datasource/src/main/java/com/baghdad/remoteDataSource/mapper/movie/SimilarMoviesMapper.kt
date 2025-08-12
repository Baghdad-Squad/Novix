package com.baghdad.remoteDataSource.mapper.movie

import com.baghdad.remoteDataSource.response.movie.SimilarMovieResponse
import com.baghdad.remoteDataSource.util.getImageUrlFromPath
import com.baghdad.repository.model.GenreDto
import com.baghdad.repository.model.MovieDto

fun SimilarMovieResponse.toMovieDtos(): List<MovieDto> = results.orEmpty().mapNotNull { it.toMovieDtoIfValid() }

private fun SimilarMovieResponse.MovieResult?.toMovieDtoIfValid(): MovieDto? = this?.takeIf { id != null }?.toMovieDto()

private fun SimilarMovieResponse.MovieResult.toMovieDto(): MovieDto =
    MovieDto(
        id = id ?: -1L,
        title = title.orEmpty(),
        genres = genreIds.toMovieGenreDtos(),
        imdbRating = voteAverage ?: 0.0,
        userRating = 0.0,
        releaseDate = releaseDate.takeUnless { it.isNullOrEmpty() } ?: "0001-01-01",
        overview = overview.orEmpty(),
        posterPictureURL = getImageUrlFromPath(posterPath),
        runtimeMinutes = 0,
        trailerURL = ""
    )

private fun List<Long>?.toMovieGenreDtos(): List<GenreDto> =
    this
        ?.map { genreId ->
            GenreDto(
                id = genreId,
                name = "",
                type = GenreDto.GenreType.MOVIE,
            )
    }.orEmpty()
