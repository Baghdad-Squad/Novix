package com.baghdad.domain.usecase.recentlyViewed

import com.baghdad.domain.model.search.RecentlyViewed
import com.baghdad.domain.repository.RecentlyViewedRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetRecentlyViewedUseCase(
    private val recentlyViewedRepository: RecentlyViewedRepository
) {
    suspend operator fun invoke(): Flow<List<RecentlyViewed>> {
        return recentlyViewedRepository.getAllRecentlyViewed().takeSortedByMostRecent()
    }

    private fun Flow<List<RecentlyViewed>>.takeSortedByMostRecent(): Flow<List<RecentlyViewed>> {
        return this.map { it.sortedByDescending { viewed -> viewed.viewedAt }.take(10) }
    }
}