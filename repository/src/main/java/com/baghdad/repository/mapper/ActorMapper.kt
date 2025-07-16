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
        birthDate = birthdayDate
            .takeIf { it.isNotBlank() }
            ?.let { LocalDate.parse(it) }
            ?: LocalDate(1990, 1, 1),
        deathDate = deathDate?.let {
            LocalDate.parse(it)
        },
        placeOfBirth = placeOfBirth,
        headerPictures = headerPictures,
        department = department,
    )
}
