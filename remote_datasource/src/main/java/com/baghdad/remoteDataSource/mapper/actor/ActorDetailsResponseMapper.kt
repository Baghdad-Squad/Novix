package com.baghdad.remoteDataSource.mapper.actor

import com.baghdad.remoteDataSource.response.actor.ActorDetailsResponse
import com.baghdad.repository.model.actor.ActorDetailsDto
import com.baghdad.repository.model.actor.ActorDto

fun ActorDetailsResponse.toDto(): ActorDetailsDto {
    val actorDto = ActorDto(
        id = this.id?.toLong() ?: 0,
        name = this.name.orEmpty(),
        imageUrl = this.profilePath.orEmpty()
    )

    return ActorDetailsDto(
        actorDto = actorDto,
        biography = this.biography.orEmpty(),
        birthday = this.birthday.orEmpty(),
        deathday = this.deathday.orEmpty(),
        homepage = this.homepage.orEmpty(),
        knownForDepartment = this.knownForDepartment.orEmpty(),
        placeOfBirth = this.placeOfBirth.orEmpty()
    )
}
