package com.baghdad.remoteDataSource.mapper

import com.baghdad.remoteDataSource.response.UserListDto
import com.baghdad.remoteDataSource.response.UserListsResponse
import com.baghdad.remoteDataSource.util.getNextKey
import com.baghdad.remoteDataSource.util.getPreviousKey
import com.baghdad.repository.model.PagedResultDto
import com.baghdad.repository.model.SavedListDto


fun UserListsResponse.toPagedSavedListsDtos(): PagedResultDto<SavedListDto> {
    return PagedResultDto(
        data = this.results?.takeIf { it.isNotEmpty()}?.map{ it.toSavedListDto() } ?: emptyList(),
        nextKey = getNextKey(page, this.totalPages),
        prevKey = getPreviousKey(page)
    )
}

internal fun UserListDto.toSavedListDto(): SavedListDto {
    return SavedListDto(
        id = this.id ?: 0,
        name = this.name.orEmpty(),
        itemCount = this.itemCount ?: 0,
    )
}