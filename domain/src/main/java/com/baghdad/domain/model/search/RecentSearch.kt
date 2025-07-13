package com.baghdad.domain.model.search

import kotlinx.datetime.LocalDateTime

data class RecentSearch(
    val id: Long,
    val query: String,
    val searchedAt: LocalDateTime
)