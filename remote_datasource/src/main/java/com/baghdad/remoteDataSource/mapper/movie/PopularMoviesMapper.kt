package com.baghdad.remoteDataSource.mapper.movie

import com.baghdad.remoteDataSource.response.movie.PopularMoviesResponse
import com.baghdad.remoteDataSource.util.getImageUrlFromPath
import com.baghdad.repository.model.GenreDto
import com.baghdad.repository.model.MovieDto

fun PopularMoviesResponse.toMovieDtos(): List<MovieDto> {
    return results.orEmpty().mapNotNull { it.toMovieDtoIfValid() }
}

private fun PopularMoviesResponse.Result?.toMovieDtoIfValid(): MovieDto? {
    return this?.takeIf { id != null }?.toMovieDto()
}

private fun PopularMoviesResponse.Result.toMovieDto(): MovieDto {
    return MovieDto(
        id = id ?: -1L,
        title = title.orEmpty(),
        genres = genreIds.toMovieGenreDtos(),
        imdbRating = voteAverage ?: 0.0,
        userRating = 0.0,
        releaseDate = releaseDate.takeUnless { it.isNullOrEmpty() } ?: "0001-01-01",
        overview = overview.orEmpty(),
        posterPictureURL = getImageUrlFromPath(posterPath),
        trailerURL = "",
        runtimeMinutes = 0
    )
}

private fun List<Long?>?.toMovieGenreDtos(): List<GenreDto> {
    return this
        ?.mapNotNull { genreId ->
            genreId?.let {
                GenreDto(
                    id = it,
                    name = "",
                    type = GenreDto.GenreType.MOVIE,
                )
            }
        }.orEmpty()
}