package com.baghdad.remoteDataSource.mapper.savedList

import com.baghdad.remoteDataSource.response.savedList.UserListsResponse
import com.baghdad.remoteDataSource.util.getNextKey
import com.baghdad.remoteDataSource.util.getPreviousKey
import com.baghdad.repository.model.PagedResultDto
import com.baghdad.repository.model.SavedListDto

fun UserListsResponse.toPagedSavedListsDtos(): PagedResultDto<SavedListDto> {
    return PagedResultDto(
        data = this.toSavedListDtos(),
        nextKey = getNextKey(page, totalPages),
        prevKey = getPreviousKey(page)
    )
}

private fun UserListsResponse.toSavedListDtos(): List<SavedListDto> {
    return results.orEmpty().map { it.toSavedListDto() }
}

private fun UserListsResponse.UserListDto.toSavedListDto(): SavedListDto {
    return SavedListDto(
        id = id ?: -1L,
        name = name.orEmpty(),
        itemCount = itemCount ?: 0,
    )
}
