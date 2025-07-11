package com.baghdad.remote_datasource.mapper

import com.baghdad.remote_datasource.entity.MultiMediaItemDto
import com.baghdad.repository.model.ActorDto
import com.baghdad.repository.model.CastMemberDto

internal fun MultiMediaItemDto.toCastMember(): CastMemberDto? {
    return if (this.mediaType == "person" && this.id != null) {
        CastMemberDto(
            actor = ActorDto(
                id = this.id,
                name = this.tvShowName ?: "Unknown Actor",
                imageUrl = this.profilePath ?: ""
            ),
            characterName = this.knownForDepartment ?: "Unknown Character"
        )
    } else {
        null
    }
}

fun getCastMembers(items: List<MultiMediaItemDto>): List<CastMemberDto> {
    return items.map{
        it.toCastMember() ?: CastMemberDto(
            actor = ActorDto(
                id = -1,
                name = "Unknown Actor",
                imageUrl = ""
            ),
            characterName = "Unknown Character"
        )
    }
}