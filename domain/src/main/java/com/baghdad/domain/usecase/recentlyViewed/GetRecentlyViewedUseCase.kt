package com.baghdad.domain.usecase.recentlyViewed

import com.baghdad.domain.model.search.RecentlyViewed
import com.baghdad.domain.repository.RecentlyViewedRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetRecentlyViewedUseCase @Inject constructor(
    private val recentlyViewedRepository: RecentlyViewedRepository
) {
    suspend operator fun invoke(): Flow<List<RecentlyViewed>> = recentlyViewedRepository.getAllRecentlyViewed().sortedByMostRecent()

    private fun Flow<List<RecentlyViewed>>.sortedByMostRecent(): Flow<List<RecentlyViewed>> {
        return this.map {
            it.sortedByDescending { viewed -> viewed.viewedAt }
        }
    }
}