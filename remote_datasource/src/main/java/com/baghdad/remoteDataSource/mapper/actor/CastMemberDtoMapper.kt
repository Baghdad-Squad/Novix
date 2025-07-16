package com.baghdad.remoteDataSource.mapper.actor

import com.baghdad.remoteDataSource.response.CastMemberResponse
import com.baghdad.repository.model.CastMemberDto
import com.baghdad.repository.model.actor.ActorDto

fun CastMemberResponse.toDto(): CastMemberDto {
    return CastMemberDto(
        actor = ActorDto(
            id = (id ?: 0).toLong(),
            name = name ?: "Unknown Actor",
            imageUrl = profilePath?.let { "https://image.tmdb.org/t/p/w500$it" } ?: "",
            biography = "",
            birthdayDate = "0001-01-01",
            deathDate = null,
            placeOfBirth = "",
            headerPictures = emptyList(),
            department = knownForDepartment ?: "Unknown",
        ),
        characterName = character ?: "Unknown"
    )
}
