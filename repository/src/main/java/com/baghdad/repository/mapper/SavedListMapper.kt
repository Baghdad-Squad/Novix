package com.baghdad.repository.mapper

import com.baghdad.entity.savedList.SavedList
import com.baghdad.repository.model.SavedListDto

fun SavedListDto.toEntity(): SavedList {
    return SavedList(
        id = id,
        name = this.name,
        itemCount = this.itemCount
    )
}