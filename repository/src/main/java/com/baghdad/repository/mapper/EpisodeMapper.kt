package com.baghdad.repository.mapper

import com.baghdad.entity.media.Episode
import com.baghdad.repository.model.EpisodeDto
import kotlinx.datetime.LocalDate

fun EpisodeDto.toEntity(): Episode {
    return Episode(
        id = id,
        title = title,
        episodeNumber = episodeNumber,
        rating = rating,
        duration = duration,
        releasedDate = releasedDate?.let { LocalDate.parse(it) },
        currentSeason = currentSeason,
        overview = overview,
        headerPictures = headerPictures,
        trailerUrl = trailerUrl,
        userRating = userRating,
        genres = genres.map { it.toEntity() },
    )
}
