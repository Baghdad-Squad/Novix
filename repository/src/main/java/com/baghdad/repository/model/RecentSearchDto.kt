package com.baghdad.repository.model

data class RecentSearchDto(
    val id: Long,
    val query: String,
    val searchedAt: Long = System.currentTimeMillis()
)
