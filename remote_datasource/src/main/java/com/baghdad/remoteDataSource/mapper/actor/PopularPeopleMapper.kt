package com.baghdad.remoteDataSource.mapper.actor


import com.baghdad.remoteDataSource.response.actor.TrendingActorResponse
import com.baghdad.remoteDataSource.util.getNextKey
import com.baghdad.remoteDataSource.util.getPreviousKey
import com.baghdad.repository.model.ActorDto
import com.baghdad.repository.model.PagedResultDto

fun TrendingActorResponse.toPagedActorDtos(): PagedResultDto<ActorDto> {
    return PagedResultDto(
        data = results?.mapNotNull { it.takeIf { it.id != null }?.toDto() } ?: emptyList(),
        nextKey = getNextKey(page, totalPages),
        prevKey = getPreviousKey(page)
    )
}

fun TrendingActorResponse.TrendingActorDetails.toDto(): ActorDto {
        val actorId = id ?: 0L
        val actorName = name ?: ""
        val imageUrl = "https://image.tmdb.org/t/p/w500$profilePath"

        return ActorDto(
            id = actorId,
            name = actorName,
            imageUrl = imageUrl,
            biography = "",
            birthdayDate = null,
            deathDate = null,
            placeOfBirth = "",
            headerPictures = emptyList(),
            department = ""
        )
    }

