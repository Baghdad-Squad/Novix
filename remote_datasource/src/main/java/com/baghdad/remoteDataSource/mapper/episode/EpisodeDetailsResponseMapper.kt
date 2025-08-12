package com.baghdad.remoteDataSource.mapper.episode

import com.baghdad.remoteDataSource.response.episode.EpisodeDetailsResponse
import com.baghdad.repository.model.EpisodeDto

fun EpisodeDetailsResponse.toDto(): EpisodeDto {
    return EpisodeDto(
        id = id ?: -1L,
        title = name.orEmpty(),
        episodeNumber = episodeNumber,
        rating = voteAverage,
        duration = runtime.toString(),
        releasedDate = airDate,
        currentSeason = seasonNumber,
        overview = overview.orEmpty(),
        headerPictures = emptyList(),
        trailerUrl = "",
        userRating = 0,
        genres = emptyList()
    )
}
