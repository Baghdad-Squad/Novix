package com.baghdad.domain.usecase.recentlyViewed

import com.baghdad.domain.model.search.RecentlyViewed
import com.baghdad.domain.repository.RecentlyViewedRepository
import com.baghdad.domain.util.now
import kotlinx.datetime.LocalDateTime

class AddRecentlyViewedUseCase(
    private val recentlyViewedRepository: RecentlyViewedRepository
) {
    suspend operator fun invoke(
        contentId: Long,
        contentImageUrl: String,
        contentType: RecentlyViewed.ContentType
    ) {
        recentlyViewedRepository.addRecentlyViewed(
            RecentlyViewed(
                contentId = contentId,
                contentImageUrl = contentImageUrl,
                contentType = contentType,
                viewedAt = LocalDateTime.now()
            )
        )
    }
}