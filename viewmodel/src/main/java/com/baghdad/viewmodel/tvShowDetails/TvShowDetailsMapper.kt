package com.baghdad.viewmodel.tvShowDetails

import com.baghdad.entity.media.Episode
import com.baghdad.entity.media.Genre
import com.baghdad.entity.media.TvShow
import com.baghdad.entity.person.CastMember
import com.baghdad.viewmodel.movieDetails.roundToFirstDecimal
import com.baghdad.viewmodel.util.toDDMMMYYYYFormat
import com.baghdad.viewmodel.util.toDDMMYYYYFormat

fun TvShow.toUiState() = TvShowDetailsScreenState.TvShowInfoUiState(
    title = title,
    genres = genres.map { it.toUiState() },
    rating = averageRating.roundToFirstDecimal(),
    releaseDate = releaseDate.toDDMMYYYYFormat(),
    seasonCount = numberOfSeasons,
    overView = overview,
    trailerURL = trailerURL,
    posterPictureURL = posterImageURL,
    headerImagesURLs = headerImagesURLs
)

fun Genre.toUiState() = TvShowDetailsScreenState.GenreUiState(
    id = id,
    name = name,
)

fun CastMember.toUiState() = TvShowDetailsScreenState.CastMemberUiState(
    id = actor.id,
    name = actor.name,
    imageUrl = actor.profilePictureURL,
    characterName = characterName
)

fun Episode.toUiState() = TvShowDetailsScreenState.EpisodeUiState(
    id = id,
    name = title,
    episodeNumber = episodeNumber,
    rating = rating.roundToFirstDecimal(),
    duration = duration,
    releaseDate = releasedDate.toDDMMMYYYYFormat(),
    currentSeason = currentSeason,
)