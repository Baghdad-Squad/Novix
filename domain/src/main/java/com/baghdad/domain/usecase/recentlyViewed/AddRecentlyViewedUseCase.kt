package com.baghdad.domain.usecase.recentlyViewed

import com.baghdad.domain.repository.RecentlyViewedRepository
import com.baghdad.entity.media.Media

class AddRecentlyViewedUseCase(
    private val recentlyViewedRepository: RecentlyViewedRepository
) {
    suspend operator fun invoke(media: Media) {
        return recentlyViewedRepository.addMediaToRecentlyViewed(
            mediaId = media.id,
            mediaType = media.type
        )
    }
}