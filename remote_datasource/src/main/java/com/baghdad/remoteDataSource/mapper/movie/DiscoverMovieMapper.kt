package com.baghdad.remoteDataSource.mapper.movie

import com.baghdad.remoteDataSource.response.movie.DiscoverMovieResponse
import com.baghdad.remoteDataSource.util.getImageUrlFromPath
import com.baghdad.remoteDataSource.util.getNextKey
import com.baghdad.remoteDataSource.util.getPreviousKey
import com.baghdad.repository.model.GenreDto
import com.baghdad.repository.model.MovieDto
import com.baghdad.repository.model.PagedResultDto

fun DiscoverMovieResponse.toPagedMovieDtos(): PagedResultDto<MovieDto> {
    return PagedResultDto(
        data = toMovieDtos(),
        nextKey = getNextKey(page, totalPages),
        prevKey = getPreviousKey(page),
    )
}

fun DiscoverMovieResponse.toMovieDtos(): List<MovieDto> {
    return results.orEmpty().mapNotNull { it.toMovieDtoIfValid() }
}

private fun DiscoverMovieResponse.Result?.toMovieDtoIfValid(): MovieDto? {
    return this?.takeIf { id != null }?.toMovieDto()
}

private fun DiscoverMovieResponse.Result.toMovieDto(): MovieDto {
    return MovieDto(
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