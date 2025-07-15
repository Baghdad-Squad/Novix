package com.baghdad.remoteDataSource.mapper.actor

import com.baghdad.remoteDataSource.response.actor.ActorDetailsResponse
import com.baghdad.repository.model.actor.ActorDto

fun ActorDetailsResponse.toDto(): ActorDto {
    return ActorDto(
        id = this.id?.toLong() ?: 0,
        name = this.name.orEmpty(),
        imageUrl = this.profilePath.orEmpty(),
        biography = this.biography.orEmpty(),
        birthdayDate = this.birthday.orEmpty(),
        deathDate = this.deathday.orEmpty(),
        placeOfBirth = this.placeOfBirth.orEmpty(),
        headerPictures = listOf(this.homepage.toString()),
        department = this.knownForDepartment.orEmpty()
    )
}
