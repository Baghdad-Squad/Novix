package com.baghdad.viewmodel.actorDetails

import com.baghdad.domain.model.savedList.SavableMovie
import com.baghdad.entity.media.TvShow
import com.baghdad.entity.person.Actor
import com.baghdad.viewmodel.util.toYYYYMMDDFormat

fun SavableMovie.toMovieUI() =
    ActorDetailsScreenState.MovieUiState(
        id = movie.id,
        posterPictureURL = movie.posterImageURL,
        isSaved = isSaved,
        savedListId = listId ?: -1L,
    )

fun TvShow.toTvShowUI() =
    ActorDetailsScreenState.TvShowUiState(
        id = id,
        posterPictureURL = posterImageURL,
    )

fun Actor.toActorInfoUI() =
    ActorDetailsScreenState.ActorInfoUiState(
        name = name,
        biography = biography,
        birthdayDate = birthDate?.toYYYYMMDDFormat(),
        placeOfBirth = placeOfBirth,
        deathDate = deathDate?.toYYYYMMDDFormat(),
        headerPictures = headerPictures,
        department = department,
    )
