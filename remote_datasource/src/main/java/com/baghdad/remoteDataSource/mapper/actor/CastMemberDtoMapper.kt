package com.baghdad.remoteDataSource.mapper.actor

import com.baghdad.remoteDataSource.response.castMembers.CastMembersResponse
import com.baghdad.remoteDataSource.util.getImageUrlFromPath
import com.baghdad.repository.model.ActorDto
import com.baghdad.repository.model.CastMemberDto

fun CastMembersResponse.toCastMembers(): List<CastMemberDto> = cast?.filter { it?.id != null }?.map { it.toDto() }.orEmpty()

private fun CastMembersResponse.CastMemberResponse.toDto(): CastMemberDto =
    CastMemberDto(
        actor = ActorDto(
            id = id ?: -1L,
            name = name.orEmpty(),
            imageUrl = getImageUrlFromPath(profilePath),
            biography = "",
            birthdayDate = null,
            deathDate = null,
            placeOfBirth = "",
            headerPictures = emptyList(),
            department = knownForDepartment.orEmpty(),
        ),
        characterName = character.orEmpty(),
    )
