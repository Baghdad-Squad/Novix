package com.baghdad.remoteDataSource.mapper.actor


import com.baghdad.remoteDataSource.response.actor.ActorMovieDto
import com.baghdad.repository.model.MovieDto

fun ActorMovieDto.toDto(): MovieDto {
    return MovieDto(
        id = this.id?.toLong() ?: 0L,
        title = this.title.orEmpty(),
        genres =emptyList(),
        imdbRating = this.voteAverage ?: 0.0,
        userRating = null,
        releaseDate = this.releaseDate.orEmpty(),
        overview = this.overview.orEmpty(),
        posterPictureURL = this.posterPath.orEmpty(),
        runtimeMinutes = 0
    )
}
