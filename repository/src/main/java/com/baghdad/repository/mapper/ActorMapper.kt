package com.baghdad.repository.mapper

import com.baghdad.entity.person.Actor
import com.baghdad.repository.model.actor.ActorDto
import kotlinx.datetime.LocalDate

fun ActorDto.toEntity(): Actor {
    return Actor(
        id = id,
        name = name,
        profilePictureURL = imageUrl,
        biography = biography,
        birthDate = LocalDate.parse(birthdayDate),
        deathDate = deathDate?.let {
            LocalDate.parse(it)
        },
        placeOfBirth = placeOfBirth,
        headerPictures = headerPictures,
        department = department
    )
}
