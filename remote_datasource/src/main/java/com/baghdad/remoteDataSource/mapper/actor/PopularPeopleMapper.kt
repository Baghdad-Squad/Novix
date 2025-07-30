package com.baghdad.remoteDataSource.mapper.actor


import com.baghdad.remoteDataSource.response.actor.TrendingActorDetails
import com.baghdad.remoteDataSource.response.actor.TrendingActorResponse
import com.baghdad.remoteDataSource.util.getNextKey
import com.baghdad.remoteDataSource.util.getPreviousKey
import com.baghdad.repository.model.ActorDto
import com.baghdad.repository.model.PagedResultDto

fun TrendingActorResponse.toPagedActorDtos(): PagedResultDto<ActorDto> {
    return PagedResultDto(
        data = results?.map { it.toDto() } ?: emptyList(),
        nextKey = getNextKey(page, totalPages),
        prevKey = getPreviousKey(page)
    )
}

fun TrendingActorDetails.toDto(): ActorDto {
        val actorId = this.id?.toLong() ?: 0L
        val actorName = this.name ?: ""
        val imageUrl = "https://image.tmdb.org/t/p/w500" + this.profilePath

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

