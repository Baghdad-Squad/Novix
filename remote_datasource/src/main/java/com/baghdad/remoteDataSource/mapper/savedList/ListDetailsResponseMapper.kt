package com.baghdad.remoteDataSource.mapper.savedList

import com.baghdad.remoteDataSource.response.savedList.ListDetailsResponse
import com.baghdad.remoteDataSource.util.getNextKey
import com.baghdad.remoteDataSource.util.getPreviousKey
import com.baghdad.repository.model.PagedResultDto
import com.baghdad.repository.model.SavedListDto
import com.baghdad.repository.model.savedList.SavedListDetailsDto
import com.baghdad.repository.model.savedList.SavedListItemDto

fun ListDetailsResponse.toSavedListDetailsDto(): SavedListDetailsDto =
    SavedListDetailsDto(
        savedList =
            SavedListDto(
                id = this.id ?: -1L,
                name = this.name.orEmpty(),
                itemCount = this.itemCount ?: 0,
            ),
        pagedItems =
            PagedResultDto(
                data =
                    this.items
                        .orEmpty()
                        .filterNotNull()
                        .mapNotNull { it.toSavedListItemDto() },
                nextKey = getNextKey(this.page, this.totalPages),
                prevKey = getPreviousKey(this.page),
            ),
    )

private fun ListDetailsResponse.Item.toSavedListItemDto(): SavedListItemDto? {
    return SavedListItemDto(
        id = this.id ?: return null,
        type = mapMediaType(mediaType) ?: return null,
        title = this.title ?: this.originalTitle ?: return null,
        posterUrl = "https://image.tmdb.org/t/p/w500" + this.posterPath.orEmpty(),
    )
}

private fun mapMediaType(type: String?) =
    when (type?.lowercase()) {
        "movie" -> SavedListItemDto.Type.MOVIE
        "tv" -> SavedListItemDto.Type.TV_SHOW
        else -> null
    }
