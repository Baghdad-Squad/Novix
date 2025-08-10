package com.baghdad.remoteDataSource.mapper.movie

import com.baghdad.remoteDataSource.response.SimilarMovieResponse
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
    nextKey = getNextKey(page, this.totalPages),
    prevKey = getPreviousKey(page)
)

fun MyRatingMoviesResponse.MovieItem.toDto(): MovieDto {
    return MovieDto(
        id = this.id?.toLong() ?: 0L,
        title = this.title ?: "Unknown Title",
        genres = this.genreIds?.map { fakeNameForMovieGenreMapper(it) } ?: emptyList(),
        imdbRating = this.voteAverage ?: 0.0,
        userRating = this.rating?.toDouble(),
        releaseDate = this.releaseDate ?: "Unknown Date",
        overview = this.overview ?: "",
        posterPictureURL = this.posterPath?.let { "https://image.tmdb.org/t/p/w500$it" } ?: "",
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