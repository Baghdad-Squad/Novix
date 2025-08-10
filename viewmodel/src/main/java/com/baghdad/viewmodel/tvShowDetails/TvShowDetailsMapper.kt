package com.baghdad.viewmodel.tvShowDetails

import com.baghdad.entity.media.Episode
import com.baghdad.entity.media.Genre
import com.baghdad.entity.media.TvShow
import com.baghdad.entity.person.CastMember
import com.baghdad.viewmodel.util.roundToFirstDecimal
import com.baghdad.viewmodel.util.toDDMMMYYYYFormat
import com.baghdad.viewmodel.util.toDDMMYYYYFormat

fun TvShow.toUiState() =
    TvShowDetailsScreenState.TvShowInfoUiState(
        title = title,
        genres = genres.map { it.toUiState() },
        rating = averageRating.roundToFirstDecimal(),
        releaseDate = releaseDate.toDDMMYYYYFormat(),
        seasonCount = numberOfSeasons,
        overView = overview,
        trailerURL = trailerURL,
        userRating = userRating ?: 0,
        posterPictureURL = posterImageURL,
        headerImagesURLs = headerImagesURLs,
    )

fun Genre.toUiState() =
    TvShowDetailsScreenState.GenreUiState(
        id = id,
        name = name,
    )

fun CastMember.toUiState() =
    TvShowDetailsScreenState.CastMemberUiState(
        id = actor.id,
        name = actor.name,
        imageUrl = actor.profilePictureURL,
        characterName = characterName,
    )

fun Episode.toUiState() =
    TvShowDetailsScreenState.EpisodeUiState(
        id = id,
        name = title,
        episodeNumber = episodeNumber,
        rating = rating.toInt(),
        duration = duration.toIntOrNull() ?: 0,
        releaseDate = releasedDate?.toDDMMMYYYYFormat().orEmpty(),
        currentSeason = currentSeason,
    )
