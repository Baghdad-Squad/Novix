package com.baghdad.domain.usecase.recentlyViewed

import com.baghdad.domain.repository.RecentlyViewedRepository

class DeleteAllRecentlyViewedUseCase(
    private val recentlyViewedRepository: RecentlyViewedRepository
) {
    suspend operator fun invoke() {
        recentlyViewedRepository.deleteAllRecentlyViewed()
    }
}