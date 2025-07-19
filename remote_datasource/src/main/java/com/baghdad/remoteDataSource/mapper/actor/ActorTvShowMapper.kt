package com.baghdad.remoteDataSource.mapper.actor

import com.baghdad.remoteDataSource.response.actor.ActorTvShowDto
import com.baghdad.repository.model.TvShowDto

fun ActorTvShowDto.toDto(
): TvShowDto {
    return TvShowDto(
        id = this.id?.toLong() ?: 0L,
        title = this.name ?: this.originalName.orEmpty(),
        genres = emptyList(),
        imdbRating = this.voteAverage ?: 0.0,
        userRating = null,
        releaseDate = this.firstAirDate.takeIf { !it.isNullOrEmpty() } ?: "0001-01-01",
        overview = this.overview.orEmpty(),
        posterPictureURL = "https://image.tmdb.org/t/p/w500" + this.posterPath,
        numberOfSeasons = 0,
        trailerURL = "",
        headerImagesURLs = emptyList()
    )
}
