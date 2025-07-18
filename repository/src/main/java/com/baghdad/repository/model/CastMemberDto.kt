package com.baghdad.repository.model

import com.baghdad.repository.model.ActorDto

data class CastMemberDto(
    val actor: ActorDto,
    val characterName: String
)
