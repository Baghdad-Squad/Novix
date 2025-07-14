package com.baghdad.remoteDataSource.util.mapper

import com.baghdad.remoteDataSource.util.entity.MultiMediaItemDto
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