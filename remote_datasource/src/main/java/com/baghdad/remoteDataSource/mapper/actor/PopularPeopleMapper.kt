package com.baghdad.remoteDataSource.mapper.actor


import com.baghdad.remoteDataSource.response.actor.TrendingActorDetails
import com.baghdad.repository.model.ActorDto

fun TrendingActorDetails.toDto(): ActorDto {
    val actorId = this.id?.toLong() ?: 0L
    val actorName = this.name ?: ""
    val imageUrl = "https://image.tmdb.org/t/p/w500" + this.profilePath

    return ActorDto(
        id = actorId,
        name = actorName,
        imageUrl = imageUrl,
        biography = "",
        birthdayDate = "",
        deathDate = null,
        placeOfBirth = "",
        headerPictures = emptyList(),
        department = ""
    )
}

