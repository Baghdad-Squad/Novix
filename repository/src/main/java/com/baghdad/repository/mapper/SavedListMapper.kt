package com.baghdad.repository.mapper

import com.baghdad.entity.savedList.SavedList
import com.baghdad.repository.model.SavedListDto

fun SavedListDto.toEntity(): SavedList =
    SavedList(
        id = id,
        name = name,
        itemCount = itemCount,
    )

fun SavedList.toDto(): SavedListDto =
    SavedListDto(
        id = id,
        name = name,
        itemCount = itemCount,
    )
