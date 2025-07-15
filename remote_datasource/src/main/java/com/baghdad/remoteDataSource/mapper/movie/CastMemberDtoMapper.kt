package com.baghdad.remoteDataSource.mapper.movie

import com.baghdad.repository.model.CastMemberDto
import com.baghdad.repository.model.actor.ActorDto

fun com.baghdad.remoteDataSource.response.CastMemberResponse.toDto(): CastMemberDto {
    return CastMemberDto(
        actor = ActorDto(
            id = (id ?: 0).toLong(),
            name = name ?: "Unknown Actor",
            imageUrl = profilePath?.let { "https://image.tmdb.org/t/p/w500$it" } ?: "",
            biography = "",
            birthdayDate = "",
            deathDate = null,
            placeOfBirth = "",
            headerPictures = emptyList(),
            department = knownForDepartment ?: "Unknown"
        ),
        characterName = character ?: "Unknown"
    )
}
