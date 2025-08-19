package com.baghdad.domain.usecase.review

import com.baghdad.domain.repository.TvShowRepository
import com.baghdad.entity.media.Review
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.LocalDate
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class GetTvShowReviewsUseCaseTest {

    companion object {
        private lateinit var tvShowRepository: TvShowRepository
        private lateinit var getTvShowReviewsUseCase: GetTvShowReviewsUseCase

        private val sampleReviews = listOf(
            Review(
                id = "tv_rev1",
                authorName = "TVFanatic",
                authorAvatarUrl = "https://example.com/tv_avatar1.jpg",
                authorUsername = "Bingeworthy!",
                reviewText = "Couldn't stop watching this amazing series.",
                postedDate = LocalDate(2023, 9, 15),
                rating = 9.5
            ),
            Review(
                id = "tv_rev2",
                authorName = "SeriesCritic",
                authorAvatarUrl = "https://example.com/tv_avatar2.jpg",
                authorUsername = "Strong First Season",
                reviewText = "Excellent character development throughout the season.",
                postedDate = LocalDate(2023, 9, 20),
                rating = 8.5
            )
        )

        private val minimalReview = Review(
            id = "tv_rev3",
            authorName = "Anonymous",
            authorAvatarUrl = "",
            authorUsername = "",
            reviewText = "It was okay",
            postedDate = LocalDate(2023, 9, 25),
            rating = 5.0
        )
    }

    @BeforeEach
    fun setUp() {
        tvShowRepository = mockk(relaxed = true)
        getTvShowReviewsUseCase = GetTvShowReviewsUseCase(tvShowRepository)
    }

    @Test
    fun `invoke() should return reviews when TV show has reviews`() = runTest {
        val tvId = 1L
        coEvery { tvShowRepository.getTvShowReviews(tvId) } returns sampleReviews

        val result = getTvShowReviewsUseCase(tvId)

        assertThat(result).hasSize(2)
        assertThat(result[0].authorName).isEqualTo("TVFanatic")
        assertThat(result[1].authorUsername).isEqualTo("Strong First Season")
    }

    @Test
    fun `invoke() should return empty list when TV show has no reviews`() = runTest {
        val tvId = 2L
        coEvery { tvShowRepository.getTvShowReviews(tvId) } returns emptyList()

        val result = getTvShowReviewsUseCase(tvId)

        assertThat(result).isEmpty()
    }

    @Test
    fun `invoke() should return reviews with special characters`() = runTest {
        val tvId = 4L
        val specialReview = sampleReviews[0].copy(
            authorName = "Critic_Élite",
            reviewText = "Fantástic character arcs! 10/10 would recommend."
        )
        coEvery { tvShowRepository.getTvShowReviews(tvId) } returns listOf(specialReview)

        val result = getTvShowReviewsUseCase(tvId)

        assertThat(result[0].authorName).isEqualTo("Critic_Élite")
        assertThat(result[0].reviewText).contains("Fantástic")
    }

    @Test
    fun `invoke() should return reviews with minimal information`() = runTest {
        val tvId = 5L
        coEvery { tvShowRepository.getTvShowReviews(tvId) } returns listOf(minimalReview)

        val result = getTvShowReviewsUseCase(tvId)

        assertThat(result).hasSize(1)
        assertThat(result[0].authorAvatarUrl).isEmpty()
        assertThat(result[0].authorUsername).isEmpty()
    }

    @Test
    fun `invoke() should return reviews with episode-specific comments`() = runTest {
        val tvId = 6L
        val episodeReview = sampleReviews[1].copy(
            reviewText = "Episode 5 was particularly outstanding with its plot twists."
        )
        coEvery { tvShowRepository.getTvShowReviews(tvId) } returns listOf(episodeReview)

        val result = getTvShowReviewsUseCase(tvId)

        assertThat(result[0].reviewText).contains("Episode 5")
    }

    @Test
    fun `invoke() should return season-based reviews`() = runTest {
        val tvId = 7L
        val seasonReview = sampleReviews[0].copy(
            authorUsername = "Season 2 Review",
            reviewText = "The second season improved upon the first in every way."
        )
        coEvery { tvShowRepository.getTvShowReviews(tvId) } returns listOf(seasonReview)

        val result = getTvShowReviewsUseCase(tvId)

        assertThat(result[0].authorUsername).isEqualTo("Season 2 Review")
    }

    @Test
    fun `invoke() should make exactly one repository call`() = runTest {
        val tvId = 8L
        coEvery { tvShowRepository.getTvShowReviews(tvId) } returns sampleReviews

        getTvShowReviewsUseCase(tvId)

        coVerify(exactly = 1) { tvShowRepository.getTvShowReviews(tvId) }
    }

    @Test
    fun `invoke() should return different reviews for different TV shows`() = runTest {
        val tvId1 = 9L
        val tvId2 = 10L
        val reviewsForShow2 = listOf(
            sampleReviews[0].copy(id = "tv_rev4", authorName = "DifferentReviewer")
        )
        coEvery { tvShowRepository.getTvShowReviews(tvId1) } returns sampleReviews
        coEvery { tvShowRepository.getTvShowReviews(tvId2) } returns reviewsForShow2

        val result1 = getTvShowReviewsUseCase(tvId1)
        val result2 = getTvShowReviewsUseCase(tvId2)

        assertThat(result1).isNotEqualTo(result2)
        assertThat(result1.size).isGreaterThan(result2.size)
    }

    @Test
    fun `invoke() should return reviews with various rating values`() = runTest {
        val tvId = 11L
        val variedRatings = listOf(
            sampleReviews[0].copy(rating = 1.0),
            sampleReviews[1].copy(rating = 10.0)
        )
        coEvery { tvShowRepository.getTvShowReviews(tvId) } returns variedRatings

        val result = getTvShowReviewsUseCase(tvId)

        assertThat(result[0].rating).isEqualTo(1.0)
        assertThat(result[1].rating).isEqualTo(10.0)
    }
}