package com.baghdad.remoteDataSource.mapper.search

import com.baghdad.remoteDataSource.response.search.ActorSearchResponse
import com.baghdad.remoteDataSource.util.getNextKey
import com.baghdad.remoteDataSource.util.getPreviousKey
import com.baghdad.repository.model.PagedResultDto
import com.baghdad.repository.model.actor.ActorDto

fun ActorSearchResponse.toPagedActorDtos() = PagedResultDto(
    data = results?.mapNotNull { it?.toActorDto() } ?: emptyList(),
    nextKey = getNextKey(page, this.totalPages),
    prevKey = getPreviousKey(page)
)

private fun ActorSearchResponse.Result.toActorDto(): ActorDto {
    return ActorDto(
        id = this.id?.toLong() ?: 0L,
        name = this.name.orEmpty(),
        imageUrl = this.profilePath?.let { "https://image.tmdb.org/t/p/w500$it" }.orEmpty(),
        biography = "",
        birthdayDate = "",
        deathDate = null,
        placeOfBirth = "",
        headerPictures = emptyList(),
        department = knownForDepartment.orEmpty()
    )
}
