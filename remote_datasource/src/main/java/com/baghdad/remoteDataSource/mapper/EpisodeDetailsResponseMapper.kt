package com.baghdad.remoteDataSource.mapper

import com.baghdad.remoteDataSource.response.EpisodeDetailsResponse
import com.baghdad.repository.model.EpisodeDto

fun EpisodeDetailsResponse.toDto(): EpisodeDto {
    return EpisodeDto(
        id = this.id.toLong(),
        title = this.name.orEmpty(),
        episodeNumber = this.episodeNumber,
        rating = this.voteAverage,
        duration = "${this.runtime} min",
        releasedDate = this.airDate.orEmpty(),
        currentSeason = this.seasonNumber,
        overview = this.overview.orEmpty(),
        headerPictures = emptyList()
    )
}
