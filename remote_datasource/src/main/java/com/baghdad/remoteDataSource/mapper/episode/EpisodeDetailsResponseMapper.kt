package com.baghdad.remoteDataSource.mapper.episode

import com.baghdad.remoteDataSource.response.episode.EpisodeDetailsResponse
import com.baghdad.repository.model.EpisodeDto

fun EpisodeDetailsResponse.toDto(): EpisodeDto {
    return EpisodeDto(
        id = this.id.toLong(),
        title = this.name.orEmpty(),
        episodeNumber = this.episodeNumber,
        rating = this.voteAverage,
        duration = "${this.runtime}",
        releasedDate = this.airDate.orEmpty(),
        currentSeason = this.seasonNumber,
        overview = this.overview.orEmpty(),
        headerPictures = emptyList(),
        trailerUrl = "",
        genres = emptyList()
    )
}
