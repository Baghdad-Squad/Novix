package com.baghdad.remoteDataSource.mapper.actor

import com.baghdad.remoteDataSource.response.actor.ActorImagesResponse
import com.baghdad.repository.model.actor.ActorImagesDto

fun ActorImagesResponse.toDto(): ActorImagesDto {
    return ActorImagesDto(
        actorId = this.id?.toLong() ?: 0L,
        images = this.profiles
            ?.mapNotNull { it.filePath }
            ?: emptyList()
    )
}
