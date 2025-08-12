package com.baghdad.remoteDataSource.mapper.actor

import com.baghdad.remoteDataSource.response.actor.ActorMoviesResponse
import com.baghdad.remoteDataSource.util.getImageUrlFromPath
import com.baghdad.repository.model.MovieDto

fun ActorMoviesResponse.toMovieDtoList(): List<MovieDto> {
    return cast?.filter { it?.id != null }?.map { it.toDto() }.orEmpty()
}

private fun ActorMoviesResponse.ActorMovieDto.toDto(): MovieDto {
    return MovieDto(
        id = id ?: -1L,
        title = title.orEmpty(),
        genres = emptyList(),
        imdbRating = voteAverage ?: 0.0,
        userRating = null,
        releaseDate = releaseDate.takeUnless { it.isNullOrEmpty() } ?: "0001-01-01",
        overview = overview.orEmpty(),
        posterPictureURL = getImageUrlFromPath(posterPath),
        runtimeMinutes = 0,
        trailerURL = ""
    )
}