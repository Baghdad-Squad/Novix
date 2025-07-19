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
        releaseDate = releaseDate.takeIf { !it.isNullOrEmpty() } ?: "0001-01-01",
        overview = this.overview.orEmpty(),
        posterPictureURL = "https://image.tmdb.org/t/p/w500"+this.posterPath.orEmpty(),
        runtimeMinutes = 0,
        trailerURL = ""
    )
}
