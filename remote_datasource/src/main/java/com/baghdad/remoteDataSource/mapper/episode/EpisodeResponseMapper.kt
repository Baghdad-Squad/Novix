package com.baghdad.remoteDataSource.mapper.episode

import com.baghdad.remoteDataSource.response.tvShow.SeasonDetailResponse
import com.baghdad.repository.model.EpisodeDto

fun SeasonDetailResponse.EpisodeResponse.toDto(): EpisodeDto {
    return EpisodeDto(
        id = id ?: 0L,
        title = name.orEmpty(),
        episodeNumber = episodeNumber ?: 0,
        rating = voteAverage ?: 0.0,
        duration = "${runtime ?: 0}",
        releasedDate = airDate,
        currentSeason = seasonNumber ?: 0,
        overview = overview.orEmpty(),
        headerPictures = emptyList(),
        trailerUrl = "",
        userRating = 0,
        genres = emptyList()
    )
}
