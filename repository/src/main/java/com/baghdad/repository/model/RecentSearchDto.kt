package com.baghdad.repository.model

import com.baghdad.entity.search.RecentSearch
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

data class RecentSearchDto(
    val id: Long,
    val query: String,
    val searchedAt: Long = System.currentTimeMillis()
)


@OptIn(ExperimentalTime::class)
fun RecentSearchDto.toEntity(): RecentSearch {
    val instant = Instant.fromEpochMilliseconds(this.searchedAt)
    return RecentSearch(
        id = this.id,
        query = this.query,
        searchedAt = instant.toLocalDateTime(TimeZone.currentSystemDefault())
    )
}