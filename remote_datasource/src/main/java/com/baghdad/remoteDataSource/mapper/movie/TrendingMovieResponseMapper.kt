package com.baghdad.remoteDataSource.mapper.movie

import com.baghdad.remoteDataSource.response.movie.TrendingMovieResponse
import com.baghdad.remoteDataSource.util.getNextKey
import com.baghdad.remoteDataSource.util.getPreviousKey
import com.baghdad.repository.model.GenreDto
import com.baghdad.repository.model.MovieDto
import com.baghdad.repository.model.PagedResultDto

fun TrendingMovieResponse.toMovieDtos() = PagedResultDto(
    data = results?.mapNotNull { it?.takeIf { it.id != null }?.toDto() } ?: emptyList(),
    nextKey = getNextKey(page, this.totalPages),
    prevKey = getPreviousKey(page)
)

fun TrendingMovieResponse.Result.toDto(): MovieDto {
    return MovieDto(
        id = id ?: 0L,
        title = title.orEmpty(),
        posterPictureURL = posterPath?.let { "https://image.tmdb.org/t/p/w500$it" } ?: "",
        trailerURL = backdropPath?.let { "https://image.tmdb.org/t/p/w500$it" } ?: "",
        overview = overview.orEmpty(),
        releaseDate = releaseDate.orEmpty(),
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
