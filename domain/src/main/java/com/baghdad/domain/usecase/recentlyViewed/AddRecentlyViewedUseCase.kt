package com.baghdad.domain.usecase.recentlyViewed

import com.baghdad.domain.model.search.RecentlyViewed
import com.baghdad.domain.repository.RecentlyViewedRepository
import com.baghdad.domain.util.now
import com.baghdad.entity.media.Genre
import kotlinx.datetime.LocalDateTime
import javax.inject.Inject

class AddRecentlyViewedUseCase @Inject constructor(
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

            ),
        )
    }
}