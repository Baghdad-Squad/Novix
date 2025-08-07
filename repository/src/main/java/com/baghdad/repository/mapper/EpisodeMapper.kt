package com.baghdad.repository.mapper

import com.baghdad.entity.media.Episode
import com.baghdad.repository.model.EpisodeDto
import kotlinx.datetime.LocalDate

fun EpisodeDto.toEntity(): Episode =
    Episode(
        id = this.id,
        title = this.title,
        episodeNumber = this.episodeNumber,
        rating = this.rating,
        duration = this.duration,
        releasedDate = releasedDate?.let { LocalDate.parse(it) },
        currentSeason = this.currentSeason,
        overview = this.overview,
        headerPictures = this.headerPictures,
        trailerUrl = this.trailerUrl,
        userRating = this.userRating,
        genres = this.genres.map { it.toEntity() },
    )
