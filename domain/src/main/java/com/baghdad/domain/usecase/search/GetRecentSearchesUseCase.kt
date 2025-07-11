package com.baghdad.domain.usecase.search

import com.baghdad.domain.repository.SearchRepository
import com.baghdad.entity.search.RecentSearch
import kotlinx.coroutines.flow.Flow

class GetRecentSearchesUseCase(
    private val searchRepository: SearchRepository
) {
    suspend operator fun invoke(): Flow<List<RecentSearch>> {
        return searchRepository.getRecentSearches()
    }
}