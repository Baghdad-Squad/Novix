package com.baghdad.remoteDataSource.mapper

import com.baghdad.remoteDataSource.response.savedList.UserListsResponse
import com.baghdad.remoteDataSource.util.getNextKey
import com.baghdad.remoteDataSource.util.getPreviousKey
import com.baghdad.repository.model.PagedResultDto
import com.baghdad.repository.model.SavedListDto
fun UserListsResponse.toPagedSavedListsDtos(): PagedResultDto<SavedListDto> {
    return PagedResultDto(
        data = results?.takeIf { it.isNotEmpty()}?.map{ it.toSavedListDto() } ?: emptyList(),
        nextKey = getNextKey(page, totalPages),
        prevKey = getPreviousKey(page)
    )
}

internal fun UserListsResponse.UserListDto.toSavedListDto(): SavedListDto {
    return SavedListDto(
        id = id ?: 0,
        name = name.orEmpty(),
        itemCount = itemCount ?: 0,
    )
}