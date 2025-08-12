package com.baghdad.remoteDataSource.mapper.search

import com.baghdad.remoteDataSource.response.search.MovieSearchResponse
import com.baghdad.remoteDataSource.util.getImageUrlFromPath
import com.baghdad.remoteDataSource.util.getNextKey
import com.baghdad.remoteDataSource.util.getPreviousKey
import com.baghdad.repository.model.GenreDto
import com.baghdad.repository.model.MovieDto
import com.baghdad.repository.model.PagedResultDto

fun MovieSearchResponse.toPagedMovieDtos(genres: List<GenreDto>): PagedResultDto<MovieDto> =
    PagedResultDto(
        data = toMovieDtos(genres),
        nextKey = getNextKey(page, totalPages),
        prevKey = getPreviousKey(page),
    )

private fun MovieSearchResponse.toMovieDtos(genres: List<GenreDto>): List<MovieDto> =
    results.orEmpty().mapNotNull {
        it.toMovieDtoIfValid(genres)
    }

private fun MovieSearchResponse.Result?.toMovieDtoIfValid(genres: List<GenreDto>): MovieDto? =
    this
        ?.takeIf {
            id != null
        }?.toMovieDto(genres)

private fun MovieSearchResponse.Result.toMovieDto(
    genres: List<GenreDto>,
    userRating: Double? = null,
    runtimeMinutes: Int = 0
): MovieDto {
    return MovieDto(
        id = id ?: -1L,
        title = title.orEmpty(),
        genres = genreIds.filterMatchingGenres(genres),
        imdbRating = voteAverage ?: 0.0,
        userRating = userRating,
        releaseDate = releaseDate.orEmpty(),
        overview = overview.orEmpty(),
        posterPictureURL = getImageUrlFromPath(posterPath),
        runtimeMinutes = runtimeMinutes,
        trailerURL = "",
    )
}

private fun List<Long?>?.filterMatchingGenres(availableGenres: List<GenreDto>): List<GenreDto> {
    val validGenreIds = this?.filterNotNull()?.toSet() ?: return emptyList()
    return availableGenres.filter { genre -> validGenreIds.contains(genre.id) }
}