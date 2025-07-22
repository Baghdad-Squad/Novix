package com.baghdad.repository.mapper

import com.baghdad.entity.person.People
import com.baghdad.repository.model.PeopleDto

fun PeopleDto.toEntity(): People {
    return People(
        id = id,
        name = name,
        profilePicatralUrl = profilePictureUrl.let { "https://image.tmdb.org/t/p/w500$it" }
    )

}