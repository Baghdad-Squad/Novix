package com.baghdad.remoteDataSource.mapper

import com.baghdad.remoteDataSource.response.EpisodeResponse
import com.baghdad.repository.model.TvShowDto

fun EpisodeResponse.toDto(): TvShowDto {
    return TvShowDto(
        id = this.id?.toLong() ?: 0L,
        title = this.name.orEmpty(),
        genres = emptyList(),
        imdbRating = this.voteAverage ?: 0.0,
        userRating = null,
        releaseDate = this.airDate.orEmpty(),
        overview = this.overview.orEmpty(),
        posterPictureURL = this.stillPath.orEmpty(),
        numberOfSeasons = this.seasonNumber ?: 0
    )
}
