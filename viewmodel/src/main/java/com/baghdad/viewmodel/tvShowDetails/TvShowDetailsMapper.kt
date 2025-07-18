package com.baghdad.viewmodel.tvShowDetails

import com.baghdad.entity.media.Episode
import com.baghdad.entity.media.Genre
import com.baghdad.entity.media.TvShow
import com.baghdad.entity.person.CastMember

fun TvShow.toUiState() = TvShowDetailsScreenState.TvShowInfoUiState(
    title = title,
    genres = genres.map { it.toUiState() },
    rating = averageRating,
    releaseDate = releaseDate.toString(),
    seasonCount = numberOfSeasons,
    overView = overview,
    headerPictures = headerPictures,
    posterPictureURL = posterImageURL
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
    rating = rating,
    duration = duration,
    releaseDate = releasedDate.toString(),
    currentSeason = currentSeason,
)