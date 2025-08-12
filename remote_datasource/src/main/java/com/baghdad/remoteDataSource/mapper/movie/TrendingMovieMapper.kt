package com.baghdad.remoteDataSource.mapper.movie

import com.baghdad.remoteDataSource.response.movie.TrendingMovieResponse
import com.baghdad.remoteDataSource.util.getImageUrlFromPath
import com.baghdad.remoteDataSource.util.getNextKey
import com.baghdad.remoteDataSource.util.getPreviousKey
import com.baghdad.repository.model.GenreDto
import com.baghdad.repository.model.MovieDto
import com.baghdad.repository.model.PagedResultDto

fun TrendingMovieResponse.toPagedMovieDtos(): PagedResultDto<MovieDto> {
    return PagedResultDto(
        data = this.toMovieDtos(),
        nextKey = getNextKey(page, totalPages),
        prevKey = getPreviousKey(page),
    )
}

private fun TrendingMovieResponse.toMovieDtos(): List<MovieDto> {
    return results.orEmpty().mapNotNull { it.toMovieDtoIfValid() }
}

private fun TrendingMovieResponse.Result?.toMovieDtoIfValid(): MovieDto? {
    return this?.takeIf { id != null }?.toMovieDto()
}

private fun TrendingMovieResponse.Result.toMovieDto(): MovieDto {
    return MovieDto(
        id = id ?: -1L,
        title = title.orEmpty(),
        posterPictureURL = getImageUrlFromPath(posterPath),
        trailerURL = getImageUrlFromPath(backdropPath),
        overview = overview.orEmpty(),
        releaseDate = releaseDate.orEmpty(),
        imdbRating = voteAverage ?: 0.0,
        runtimeMinutes = 0,
        userRating = popularity ?: 0.0,
        genres = genreIds.toMovieGenreDtos(),
    )
}

private fun List<Long>?.toMovieGenreDtos(): List<GenreDto> {
    return this
        ?.map { genreId ->
            GenreDto(
                id = genreId,
                name = "",
                type = GenreDto.GenreType.MOVIE,
            )
        }.orEmpty()
}
