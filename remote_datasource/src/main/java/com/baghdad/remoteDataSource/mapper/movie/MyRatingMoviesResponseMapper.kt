package com.baghdad.remoteDataSource.mapper.movie

import com.baghdad.remoteDataSource.response.movie.MyRatingMoviesResponse
import com.baghdad.remoteDataSource.util.getNextKey
import com.baghdad.remoteDataSource.util.getPreviousKey
import com.baghdad.repository.model.GenreDto
import com.baghdad.repository.model.MovieDto
import com.baghdad.repository.model.PagedResultDto

fun MyRatingMoviesResponse.toPagedMovieDtos() = PagedResultDto(
    data = results?.mapNotNull {
        it.takeIf { it.id != null }?.toDto()
    } ?: emptyList(),
    nextKey = getNextKey(page, totalPages),
    prevKey = getPreviousKey(page)
)

fun MyRatingMoviesResponse.MovieItem.toDto(): MovieDto {
    return MovieDto(
        id = id ?: 0L,
        title = title ?: "Unknown Title",
        genres = genreIds?.map { fakeNameForMovieGenreMapper(it) } ?: emptyList(),
        imdbRating = voteAverage ?: 0.0,
        userRating = rating?.toDouble(),
        releaseDate = releaseDate ?: "Unknown Date",
        overview = overview ?: "",
        posterPictureURL = posterPath?.let { "https://image.tmdb.org/t/p/w500$it" } ?: "",
        trailerURL = "",
        runtimeMinutes = 0
    )
}

private fun fakeNameForMovieGenreMapper(genreId: Long): GenreDto {
    return GenreDto(
        id = genreId,
        name = "",
        type = GenreDto.GenreType.MOVIE
    )
}