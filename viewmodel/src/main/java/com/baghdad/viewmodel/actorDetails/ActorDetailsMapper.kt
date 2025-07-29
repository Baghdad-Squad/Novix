package com.baghdad.viewmodel.actorDetails

import com.baghdad.entity.media.Movie
import com.baghdad.entity.media.TvShow
import com.baghdad.entity.person.Actor
import com.baghdad.viewmodel.util.toYYYYMMDDFormat


fun Movie.toMovieUI() = ActorDetailsScreenState.MovieUiState(
    id = id,
    posterPictureURL = posterImageURL,
    // TODO: we need to add isSaved from domain
)

fun TvShow.toTvShowUI() = ActorDetailsScreenState.TvShowUiState(
    id = id,
    posterPictureURL = posterImageURL,
    // TODO: we need to add isSaved from domain
)

fun Actor.toActorInfoUI() = ActorDetailsScreenState.ActorInfoUiState(
    name = name,
    biography = biography,
    birthdayDate = birthDate?.toYYYYMMDDFormat(),
    placeOfBirth = placeOfBirth,
    deathDate = deathDate?.toYYYYMMDDFormat(),
    headerPictures = headerPictures,
    department = department
)
