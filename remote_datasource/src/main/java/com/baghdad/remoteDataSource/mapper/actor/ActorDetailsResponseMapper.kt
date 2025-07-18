package com.baghdad.remoteDataSource.mapper.actor

import com.baghdad.remoteDataSource.response.actor.ActorDetailsResponse
import com.baghdad.repository.model.actor.ActorDto

fun ActorDetailsResponse.toDto(): ActorDto {
    return ActorDto(
        id = this.id?.toLong() ?: 0,
        name = this.name.orEmpty(),
        imageUrl = "https://image.tmdb.org/t/p/w500"+ this.profilePath,
        biography = this.biography.orEmpty(),
        birthdayDate = this.birthday ?: "",
        deathDate = this.deathday,
        placeOfBirth = this.placeOfBirth.orEmpty(),
        headerPictures = listOf("https://image.tmdb.org/t/p/w500"+ this.profilePath),
        department = this.knownForDepartment.orEmpty()
    )
}
