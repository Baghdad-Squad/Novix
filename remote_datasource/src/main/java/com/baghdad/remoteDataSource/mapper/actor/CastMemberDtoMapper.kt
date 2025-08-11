package com.baghdad.remoteDataSource.mapper.actor

import com.baghdad.remoteDataSource.response.castMembers.CastMembersResponse
import com.baghdad.repository.model.ActorDto
import com.baghdad.repository.model.CastMemberDto

fun CastMembersResponse.CastMemberResponse.toDto(): CastMemberDto {
    return CastMemberDto(
        actor = ActorDto(
            id = id ?: 0L,
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
fun CastMembersResponse.toCastMembers(): List<CastMemberDto> {
    return cast?.mapNotNull { it.takeIf { it.id != null }?.toDto() } ?: emptyList()
}