package com.baghdad.remoteDataSource.mapper.actor

import com.baghdad.remoteDataSource.response.actor.ActorImagesResponse
import com.baghdad.remoteDataSource.util.getImageUrlFromPath

fun ActorImagesResponse.toActorDtoList(): List<String> {
    return profiles.orEmpty().mapNotNull { getImageUrlFromPath(it.filePath).ifBlank { null } }
}