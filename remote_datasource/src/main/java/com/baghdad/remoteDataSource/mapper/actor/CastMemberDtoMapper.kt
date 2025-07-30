package com.baghdad.remoteDataSource.mapper.actor

import com.baghdad.remoteDataSource.response.CastMemberResponse
import com.baghdad.repository.model.ActorDto
import com.baghdad.repository.model.CastMemberDto

fun CastMemberResponse.toDto(): CastMemberDto {
    return CastMemberDto(
        actor = ActorDto(
            id = (id ?: 0).toLong(),
            name = name ?: "Unknown Actor",
            imageUrl = profilePath?.let { "https://image.tmdb.org/t/p/w500$it" } ?: "",
            biography = "",
            birthdayDate = null,
            deathDate = null,
            placeOfBirth = "",
            headerPictures = emptyList(),
            department = knownForDepartment ?: "Unknown",
        ),
        characterName = character ?: "Unknown"
    )
}
