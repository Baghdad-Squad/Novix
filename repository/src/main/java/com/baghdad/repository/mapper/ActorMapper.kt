package com.baghdad.repository.mapper

import com.baghdad.entity.person.Actor
import com.baghdad.repository.model.actor.ActorDto

fun ActorDto.toEntity(): Actor {
    return Actor(
        id = id,
        name = name,
        profilePictureURL = imageUrl
    )
}
