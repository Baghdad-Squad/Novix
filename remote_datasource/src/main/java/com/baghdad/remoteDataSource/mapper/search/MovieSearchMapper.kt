package com.baghdad.remoteDataSource.mapper.search

import com.baghdad.remoteDataSource.response.search.MovieSearchResponse
import com.baghdad.remoteDataSource.util.getNextKey
import com.baghdad.remoteDataSource.util.getPreviousKey
import com.baghdad.repository.model.GenreDto
import com.baghdad.repository.model.MovieDto
import com.baghdad.repository.model.PagedResultDto

fun MovieSearchResponse.toPagedMovieDtos(genres: List<GenreDto>) = PagedResultDto(
    data = results?.mapNotNull { it?.takeIf { it.id != null }?.toMovieDto(genres) } ?: emptyList(),
    nextKey = getNextKey(page, this.totalPages),
    prevKey = getPreviousKey(page)
)

internal fun MovieSearchResponse.Result.toMovieDto(
    genres: List<GenreDto> = emptyList(),
    userRating: Double? = null,
    runtimeMinutes: Int = 0
): MovieDto {
    return MovieDto(
        id = this.id?.toLong() ?: 0L,
        title = this.title.orEmpty(),
        genres = filterGenres(this.genreIds ?: emptyList<Int>(), genres),
        imdbRating = this.voteAverage ?: 0.0,
        userRating = userRating,
        releaseDate = this.releaseDate.orEmpty(),
        overview = this.overview.orEmpty(),
        posterPictureURL = this.posterPath?.let { "https://image.tmdb.org/t/p/w500$it" }.orEmpty(),
        runtimeMinutes = runtimeMinutes,
        trailerURL = ""
    )
}

private fun filterGenres(
    genreIds: List<Int?>,
    genres: List<GenreDto>
): List<GenreDto> {
    return genres.filter { genre ->
        genreIds.contains(genre.id.toInt())
    }
}