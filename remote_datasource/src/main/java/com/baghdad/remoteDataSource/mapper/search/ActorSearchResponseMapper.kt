package com.baghdad.remoteDataSource.mapper.search

import com.baghdad.remoteDataSource.response.search.ActorSearchResponse
import com.baghdad.remoteDataSource.util.getNextKey
import com.baghdad.remoteDataSource.util.getPreviousKey
import com.baghdad.repository.model.ActorDto
import com.baghdad.repository.model.PagedResultDto

fun ActorSearchResponse.toPagedActorDtos() = PagedResultDto(
    data = results?.mapNotNull { it?.takeIf { it.id != null }?.toActorDto() } ?: emptyList(),
    nextKey = getNextKey(page, totalPages),
    prevKey = getPreviousKey(page)
)

private fun ActorSearchResponse.Result.toActorDto(): ActorDto {
    return ActorDto(
        id = id ?: 0L,
        name = name.orEmpty(),
        imageUrl = profilePath?.let { "https://image.tmdb.org/t/p/w500$it" }.orEmpty(),
        biography = "",
        birthdayDate = null,
        deathDate = null,
        placeOfBirth = "",
        headerPictures = emptyList(),
        department = knownForDepartment.orEmpty()
    )
}
