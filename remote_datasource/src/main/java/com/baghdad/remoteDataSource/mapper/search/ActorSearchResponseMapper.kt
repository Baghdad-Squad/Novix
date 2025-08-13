package com.baghdad.remoteDataSource.mapper.search

import com.baghdad.remoteDataSource.response.search.ActorSearchResponse
import com.baghdad.remoteDataSource.util.getImageUrlFromPath
import com.baghdad.remoteDataSource.util.getNextKey
import com.baghdad.remoteDataSource.util.getPreviousKey
import com.baghdad.repository.model.ActorDto
import com.baghdad.repository.model.PagedResultDto

fun ActorSearchResponse.toPagedActorDtos(): PagedResultDto<ActorDto> {
    return PagedResultDto(
        data = toActorDtos(),
        nextKey = getNextKey(page, totalPages),
        prevKey = getPreviousKey(page)
    )
}

private fun ActorSearchResponse.toActorDtos(): List<ActorDto> {
    return results.orEmpty().mapNotNull { it.toActorDtoIfValid() }
}

private fun ActorSearchResponse.Result?.toActorDtoIfValid(): ActorDto? {
    return this?.takeIf { id != null }?.toActorDto()
}


private fun ActorSearchResponse.Result.toActorDto(): ActorDto {
    return ActorDto(
        id = id ?: -1L,
        name = name.orEmpty(),
        imageUrl = getImageUrlFromPath(profilePath),
        biography = "",
        birthdayDate = null,
        deathDate = null,
        placeOfBirth = "",
        headerPictures = emptyList(),
        department = knownForDepartment.orEmpty()
    )
}