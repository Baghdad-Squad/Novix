package com.baghdad.remoteDataSource.mapper.actor

import com.baghdad.remoteDataSource.response.actor.ActorDetailsResponse
import com.baghdad.remoteDataSource.util.getImageUrlFromPath
import com.baghdad.repository.model.ActorDto

fun ActorDetailsResponse.toDto(): ActorDto {
    return ActorDto(
        id = id ?: -1L,
        name = name.orEmpty(),
        imageUrl = getImageUrlFromPath(profilePath),
        biography = biography.orEmpty(),
        birthdayDate = birthday,
        deathDate = deathday,
        placeOfBirth = placeOfBirth.orEmpty(),
        headerPictures = listOf(getImageUrlFromPath(profilePath)),
        department = knownForDepartment.orEmpty(),
    )
}