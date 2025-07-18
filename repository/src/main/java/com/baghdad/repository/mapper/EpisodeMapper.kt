package com.baghdad.repository.mapper

import com.baghdad.entity.media.Episode
import com.baghdad.repository.model.EpisodeDto
import kotlinx.datetime.toLocalDate

fun EpisodeDto.toEntity(): Episode {
    return Episode(
        id = this.id,
        title = this.title,
        episodeNumber = this.episodeNumber,
        rating = this.rating,
        duration = this.duration,
        releasedDate = this.releasedDate.toLocalDate(),
        currentSeason = this.currentSeason,
        overview = this.overview,
        headerPictures = this.headerPictures
    )
}
