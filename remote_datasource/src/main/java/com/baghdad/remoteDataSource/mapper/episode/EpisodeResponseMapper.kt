package com.baghdad.remoteDataSource.mapper.episode

import com.baghdad.remoteDataSource.response.tvShow.EpisodeResponse
import com.baghdad.repository.model.EpisodeDto

fun EpisodeResponse.toDto(): EpisodeDto {
    return EpisodeDto(
        id = this.id?.toLong() ?: 0L,
        title = this.name.orEmpty(),
        episodeNumber = this.episodeNumber ?: 0,
        rating = this.voteAverage ?: 0.0,
        duration = "${this.runtime ?: 0}",
        releasedDate = this.airDate ?: "0001-01-01",
        currentSeason = this.seasonNumber ?: 0,
        overview = this.overview.orEmpty(),
        headerPictures = emptyList(),
        trailerUrl = "",
        genres = emptyList()
    )
}
