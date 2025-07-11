package com.baghdad.domain.testHelper

import com.baghdad.entity.search.RecentSearch
import kotlinx.datetime.LocalDateTime

fun getTestRecentSearch(
    id: Long = 1L,
    query: String = "Test Query",
    searchedAt: LocalDateTime = LocalDateTime(2023, 10, 1, 12, 0, 0)
): RecentSearch = RecentSearch(
    id = id,
    query = query,
    searchedAt = searchedAt
)

fun getTestRecentSearches(
    count: Int
): List<RecentSearch> = (1..count).map { index ->
    RecentSearch(
        id = index.toLong(),
        query = "Test Query $index",
        searchedAt = LocalDateTime(2023, 10, 1, 12, 0, index % 60)
    )
}