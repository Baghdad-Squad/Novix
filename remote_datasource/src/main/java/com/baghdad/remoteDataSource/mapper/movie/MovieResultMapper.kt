package com.baghdad.remoteDataSource.mapper.movie

import com.baghdad.remoteDataSource.response.MovieResult
import com.baghdad.remoteDataSource.response.SimilarMovieResponse
import com.baghdad.remoteDataSource.util.getNextKey
import com.baghdad.remoteDataSource.util.getPreviousKey
import com.baghdad.repository.model.MovieDto
import com.baghdad.repository.model.PagedResultDto


fun SimilarMovieResponse.toPagedMovieDtos() = PagedResultDto(
    data = results?.map { it.toDto() } ?: emptyList(),
    nextKey = getNextKey(page, this.totalPages),
    prevKey = getPreviousKey(page)
)

fun MovieResult.toDto(
    genreIds: List<Int>? = null,
): MovieDto {

    return MovieDto(
        id = (id ?: 0).toLong(),
        title = title ?: "Untitled",
        genres = genreIds?.map { GenreDto(it.toLong(), "", GenreDto.GenreType.MOVIE) }
            ?: emptyList(),
        imdbRating = voteAverage ?: 0.0,
        userRating = 0.0,
        releaseDate = releaseDate.takeIf { !it.isNullOrEmpty() } ?: "0001-01-01",
        overview = overview ?: "No overview available.",
        posterPictureURL = posterPath?.let { "https://image.tmdb.org/t/p/w500$it" } ?: "",
        runtimeMinutes = 0,
        trailerURL = ""
    )
}
