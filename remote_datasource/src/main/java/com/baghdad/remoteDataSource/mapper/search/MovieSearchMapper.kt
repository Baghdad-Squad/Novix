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
        id = id ?: 0L,
        title = title.orEmpty(),
        genres = filterGenres(genreIds ?: emptyList<Long>(), genres),
        imdbRating = voteAverage ?: 0.0,
        userRating = userRating,
        releaseDate = releaseDate.orEmpty(),
        overview = overview.orEmpty(),
        posterPictureURL = posterPath?.let { "https://image.tmdb.org/t/p/w500$it" }.orEmpty(),
        runtimeMinutes = runtimeMinutes,
        trailerURL = ""
    )
}
private fun filterGenres(
    genreIds: List<Long?>,
    genres: List<GenreDto>
): List<GenreDto> {
    val nonNullGenreIds = genreIds.filterNotNull().map { it }
    return genres.filter { genre ->
        nonNullGenreIds.contains(genre.id)
    }
}