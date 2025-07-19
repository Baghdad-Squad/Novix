package com.baghdad.viewmodel.episodeDetails

import com.baghdad.entity.media.Episode
import com.baghdad.entity.media.Genre
import com.baghdad.entity.person.CastMember

fun Episode.toUiState() = EpisodeDetailsScreenState.EpisodeUiState(
    id = id,
    title = title,
    episodeNumber = episodeNumber,
    rating = rating,
    duration = duration,
    releasedDate = releasedDate.toString(),
    currentSeason = currentSeason,
    overview = overview,
    headerPictures = headerPictures,
    categories = genres.toUiStates(),
)

fun List<Genre>.toUiStates() = this.map {
    EpisodeDetailsScreenState.CategoryUiState(
        id = it.id,
        name = it.name
    )
}

fun CastMember.toUiState() = EpisodeDetailsScreenState.GuestsOfHonerUiState(
    id = actor.id,
    name = actor.name,
    profilePictureURL = actor.profilePictureURL,
    characterName = characterName
)