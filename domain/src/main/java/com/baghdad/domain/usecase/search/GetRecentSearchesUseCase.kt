package com.baghdad.domain.usecase.search

import com.baghdad.domain.repository.SearchRepository
import com.baghdad.entity.search.RecentSearch
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetRecentSearchesUseCase(
    private val searchRepository: SearchRepository
) {
    operator fun invoke(): Flow<List<RecentSearch>> {
        return searchRepository.getRecentSearches().sortedByMostRecent()
    }

    private fun Flow<List<RecentSearch>>.sortedByMostRecent(): Flow<List<RecentSearch>> {
        return this.map { it.sortedByDescending { search -> search.searchedAt } }
    }
}