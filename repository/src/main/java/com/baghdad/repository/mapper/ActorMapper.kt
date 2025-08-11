package com.baghdad.repository.mapper

import com.baghdad.entity.person.Actor
import com.baghdad.repository.model.ActorDto
import com.baghdad.repository.model.SearchQueryDto
import kotlinx.datetime.LocalDate

fun ActorDto.toEntity(): Actor {
    return Actor(
        id = id,
        name = name,
        profilePictureURL = imageUrl,
        biography = biography,
        birthDate = birthdayDate?.let {
            LocalDate.parse(it)
        },
        deathDate = deathDate?.let {
            LocalDate.parse(it)
        },
        placeOfBirth = placeOfBirth,
        headerPictures = headerPictures,
        department = department
    )
}

/**
 * we wanna change exaction  fun to dto
 * طب ليهه
 * نشيليهااااااااا
 */
fun ActorDto.toSearchQueryDto(query: String): SearchQueryDto {
    return SearchQueryDto(
        queryName = query,
        mediaId = id,
        mediaType = SearchQueryDto.MediaType.ACTOR
    )
}
