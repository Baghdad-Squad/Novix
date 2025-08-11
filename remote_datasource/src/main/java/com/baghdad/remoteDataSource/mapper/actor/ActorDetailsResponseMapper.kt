package com.baghdad.remoteDataSource.mapper.actor

import com.baghdad.remoteDataSource.response.actor.ActorDetailsResponse
import com.baghdad.repository.model.ActorDto

fun ActorDetailsResponse.toDto(): ActorDto {
    return ActorDto(
        id = id ?: 0,
        name = name.orEmpty(),
        imageUrl = "https://image.tmdb.org/t/p/w500$profilePath",
        biography = biography.orEmpty(),
        birthdayDate = birthday,
        deathDate = deathday,
        placeOfBirth = placeOfBirth.orEmpty(),
        headerPictures = listOf("https://image.tmdb.org/t/p/w500$profilePath"),
        department = knownForDepartment.orEmpty(),
    )
}
