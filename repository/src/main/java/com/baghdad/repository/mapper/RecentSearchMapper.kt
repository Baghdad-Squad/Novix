package com.baghdad.repository.mapper

import com.baghdad.entity.search.RecentSearch
import com.baghdad.repository.model.RecentSearchDto
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

@OptIn(ExperimentalTime::class)
fun RecentSearchDto.toEntity(): RecentSearch {
    return RecentSearch(
        id = id,
        query = query,
        searchedAt = Instant.fromEpochMilliseconds(epochMilliseconds = searchedAt)
            .toLocalDateTime(TimeZone.currentSystemDefault())
    )
}