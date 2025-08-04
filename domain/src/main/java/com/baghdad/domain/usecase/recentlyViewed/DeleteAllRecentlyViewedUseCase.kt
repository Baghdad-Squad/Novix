package com.baghdad.domain.usecase.recentlyViewed

import com.baghdad.domain.repository.RecentlyViewedRepository
import javax.inject.Inject

class DeleteAllRecentlyViewedUseCase @Inject constructor(
    private val recentlyViewedRepository: RecentlyViewedRepository
) {
    suspend operator fun invoke() {
        recentlyViewedRepository.deleteAllRecentlyViewed()
    }
}