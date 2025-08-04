package com.baghdad.repository.mapper

import com.baghdad.domain.model.savedList.SavedListDetails
import com.baghdad.repository.model.savedList.SavedListDetailsDto
import com.baghdad.repository.model.savedList.SavedListItemDto

fun SavedListDetailsDto.toEntity() =
    SavedListDetails(
        savedList = savedList.toEntity(),
        pagedItems = pagedItems.toPagedResult(SavedListItemDto::toEntity),
    )
