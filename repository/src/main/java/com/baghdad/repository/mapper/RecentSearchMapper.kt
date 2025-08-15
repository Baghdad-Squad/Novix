package com.baghdad.repository.mapper

import com.baghdad.entity.search.RecentSearch
import com.baghdad.repository.model.RecentSearchDto
import com.baghdad.repository.util.convertMillisToLocalDateTime
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
fun RecentSearchDto.toEntity(): RecentSearch {
    return RecentSearch(
        id = id,
        query = query,
        searchedAt = convertMillisToLocalDateTime(searchedAt)
    )
}