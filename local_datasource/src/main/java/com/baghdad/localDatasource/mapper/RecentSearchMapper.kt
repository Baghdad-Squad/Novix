package com.baghdad.localDatasource.mapper

import com.baghdad.localDatasource.roomDB.entity.RecentSearch
import com.baghdad.repository.model.RecentSearchDto

fun RecentSearch.toDto(): RecentSearchDto {
    return RecentSearchDto(
        id = this.id,
        query = this.query,
        searchedAt = this.searchedAt
    )
}