package com.baghdad.repository.mapper

import com.baghdad.entity.savedList.SavedList
import com.baghdad.repository.model.SavedListDto

fun SavedListDto.toEntity(): SavedList {
    return SavedList(
        id = this.id,
        name = this.name,
        itemCount = this.itemCount,
    )
}