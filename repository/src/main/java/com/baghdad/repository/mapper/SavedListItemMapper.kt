package com.baghdad.repository.mapper

import com.baghdad.domain.model.savedList.SavedListItem
import com.baghdad.repository.model.savedList.SavedListItemDto

fun SavedListItemDto.toEntity() =
    SavedListItem(
        id = id,
        type = SavedListItem.Type.valueOf(type.name),
        title = title,
        posterUrl = posterUrl,
    )
