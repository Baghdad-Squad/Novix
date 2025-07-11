package com.baghdad.domain.usecase.search

import com.baghdad.entity.search.RecentSearch
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class GetRecentSearchesUseCase {
    suspend operator fun invoke(): Flow<List<RecentSearch>> {
        //TODO("Not yet implemented")
        return flowOf(emptyList())
    }
}