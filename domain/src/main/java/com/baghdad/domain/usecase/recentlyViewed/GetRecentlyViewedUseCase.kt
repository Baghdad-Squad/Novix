package com.baghdad.domain.usecase.recentlyViewed

import com.baghdad.domain.repository.RecentlyViewedRepository
import com.baghdad.entity.media.Media
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetRecentlyViewedUseCase(
    private val recentlyViewedRepository: RecentlyViewedRepository
) {
    suspend operator fun invoke(): Flow<List<Media>> {
        return recentlyViewedRepository.getAllRecentlyViewed().map { it.take(10) }
    }
}