package com.baghdad.repository.mapper

import com.baghdad.domain.model.search.RecentSearch
import com.baghdad.repository.model.RecentSearchDto
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

@OptIn(ExperimentalTime::class)
fun RecentSearchDto.toEntity(): RecentSearch {
    return RecentSearch(
        id = this.id,
        query = this.query,
        searchedAt = Instant.fromEpochMilliseconds(this.searchedAt)
            .toLocalDateTime(TimeZone.currentSystemDefault())
    )
}