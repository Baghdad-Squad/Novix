package com.baghdad.domain.usecase.review

import com.baghdad.domain.repository.TvShowRepository
import com.baghdad.entity.media.Review

class GetTvShowReviewsUseCase(
    private val tvShowRepository: TvShowRepository
) {
    suspend operator fun invoke(tvId: Long): List<Review> {
        return tvShowRepository.getTvShowReviews(tvId)
    }
}