package com.baghdad.remoteDataSource.mapper.people


import com.baghdad.remoteDataSource.response.people.PersonDetails
import com.baghdad.repository.model.PeopleDto

fun PersonDetails.toDto(): PeopleDto {
    return PeopleDto(
        id = id.toLong(),
        name = name,
        profilePictureUrl = profilePath?.let { "https://image.tmdb.org/t/p/w500$it" } ?: ""
    )

}