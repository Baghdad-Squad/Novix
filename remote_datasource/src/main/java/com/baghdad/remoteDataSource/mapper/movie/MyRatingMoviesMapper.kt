package com.baghdad.remoteDataSource.mapper.movie

import com.baghdad.remoteDataSource.response.movie.MyRatingMoviesResponse
import com.baghdad.remoteDataSource.util.getImageUrlFromPath
import com.baghdad.remoteDataSource.util.getNextKey
import com.baghdad.remoteDataSource.util.getPreviousKey
import com.baghdad.repository.model.GenreDto
import com.baghdad.repository.model.MovieDto
import com.baghdad.repository.model.PagedResultDto

fun MyRatingMoviesResponse.toPagedMovieDtos(): PagedResultDto<MovieDto> =
    PagedResultDto(
        data = toMovieDtos(),
        nextKey = getNextKey(page, totalPages),
        prevKey = getPreviousKey(page),
    )

private fun MyRatingMoviesResponse.toMovieDtos(): List<MovieDto> = results.orEmpty().mapNotNull { it.toMovieDtoIfValid() }

private fun MyRatingMoviesResponse.MovieItem?.toMovieDtoIfValid(): MovieDto? = this?.takeIf { id != null }?.toMovieDto()

private fun MyRatingMoviesResponse.MovieItem.toMovieDto(): MovieDto =
    MovieDto(
        id = id ?: -1L,
        title = title.orEmpty(),
        genres = genreIds.toMovieGenreDtos(),
        imdbRating = voteAverage ?: 0.0,
        userRating = rating?.toDouble(),
        releaseDate = releaseDate ?: "0001-01-01",
        overview = overview.orEmpty(),
        posterPictureURL = getImageUrlFromPath(posterPath),
        trailerURL = "",
        runtimeMinutes = 0
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
