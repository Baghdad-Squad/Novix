package com.baghdad.remoteDataSource.mapper.actor

import com.baghdad.remoteDataSource.response.actor.TrendingActorResponse
import com.baghdad.remoteDataSource.util.getImageUrlFromPath
import com.baghdad.remoteDataSource.util.getNextKey
import com.baghdad.remoteDataSource.util.getPreviousKey
import com.baghdad.repository.model.ActorDto
import com.baghdad.repository.model.PagedResultDto

fun TrendingActorResponse.toPagedActorDtos(): PagedResultDto<ActorDto> {
    return PagedResultDto(
        data = results?.filter { it?.id != null }?.map { it.toDto() }.orEmpty(),
        nextKey = getNextKey(page, totalPages),
        prevKey = getPreviousKey(page)
    )
}

private fun TrendingActorResponse.TrendingActorDetails.toDto(): ActorDto =
    ActorDto(
        id = id ?: -1L,
        name = name ?: "",
        imageUrl = getImageUrlFromPath(profilePath),
        biography = "",
        birthdayDate = null,
        deathDate = null,
        placeOfBirth = "",
        headerPictures = emptyList(),
        department = "",
    )
