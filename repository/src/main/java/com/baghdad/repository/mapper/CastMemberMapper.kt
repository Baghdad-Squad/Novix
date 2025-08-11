package com.baghdad.repository.mapper

import com.baghdad.entity.person.CastMember
import com.baghdad.repository.model.CastMemberDto

fun CastMemberDto.toEntity(): CastMember {
    return CastMember(
        actor = actor.toEntity(),
        characterName = characterName
    )
}