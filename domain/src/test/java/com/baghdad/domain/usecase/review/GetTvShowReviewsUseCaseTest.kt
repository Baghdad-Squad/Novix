package com.baghdad.domain.usecase.review

import com.baghdad.domain.repository.TvShowRepository
import com.baghdad.domain.usecase.tvShow.TvShowMock
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

class GetTvShowReviewsUseCaseTest {

    private val tvShowRepository = mockk<TvShowRepository>()
    private val getTvShowReviewsUseCase = GetTvShowReviewsUseCase(tvShowRepository)

    @Test
    fun `getTvShowReviewsUseCase should return reviews when TV show has reviews`() = runTest {
        coEvery { tvShowRepository.getTvShowReviews(tvShowId) } returns reviews

        val result = getTvShowReviewsUseCase(tvShowId)

        assertThat(result).isEqualTo(reviews)
    }

    private companion object {
        val tvShowId = TvShowMock.TV_SHOW_ID
        val reviews = ReviewMock.REVIEWS
    }
}