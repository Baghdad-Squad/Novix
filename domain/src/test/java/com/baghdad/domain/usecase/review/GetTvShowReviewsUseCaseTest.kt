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


    @BeforeEach
    fun setUp() {
        tvShowRepository = mockk(relaxed = true)
        getTvShowReviewsUseCase = GetTvShowReviewsUseCase(tvShowRepository)
    }

    @Test
    fun `getTvShowReviewsUseCase returns reviews when TV show has reviews`() = runTest {
        // Given
        val tvId = 1L
        coEvery { tvShowRepository.getTvShowReviews(tvId) } returns sampleReviews

        // When
        val result = getTvShowReviewsUseCase(tvId)

        // Then
        assertThat(result).hasSize(2)
        assertThat(result[0].authorName).isEqualTo("TVFanatic")
        assertThat(result[1].contentTitle).isEqualTo("Strong First Season")
    }

    @Test
    fun `getTvShowReviewsUseCase returns empty list when TV show has no reviews`() = runTest {
        // Given
        val tvId = 2L
        coEvery { tvShowRepository.getTvShowReviews(tvId) } returns emptyList()

        // When
        val result = getTvShowReviewsUseCase(tvId)

        // Then
        assertThat(result).isEmpty()
    }

    @Test
    fun `getTvShowReviewsUseCase returns reviews with special characters`() = runTest {
        // Given
        val tvId = 4L
        val specialReview = sampleReviews[0].copy(
            authorName = "Critic_Élite",
            reviewText = "Fantástic character arcs! 10/10 would recommend."
        )
        coEvery { tvShowRepository.getTvShowReviews(tvId) } returns listOf(specialReview)

        // When
        val result = getTvShowReviewsUseCase(tvId)

        // Then
        assertThat(result[0].authorName).isEqualTo("Critic_Élite")
        assertThat(result[0].reviewText).contains("Fantástic")
    }

    @Test
    fun `getTvShowReviewsUseCase returns reviews with minimal information`() = runTest {
        // Given
        val tvId = 5L
        coEvery { tvShowRepository.getTvShowReviews(tvId) } returns listOf(minimalReview)

        // When
        val result = getTvShowReviewsUseCase(tvId)

        // Then
        assertThat(result).hasSize(1)
        assertThat(result[0].authorAvatarUrl).isEmpty()
        assertThat(result[0].contentTitle).isEmpty()
    }

    @Test
    fun `getTvShowReviewsUseCase returns reviews with episode-specific comments`() = runTest {
        // Given
        val tvId = 6L
        val episodeReview = sampleReviews[1].copy(
            reviewText = "Episode 5 was particularly outstanding with its plot twists."
        )
        coEvery { tvShowRepository.getTvShowReviews(tvId) } returns listOf(episodeReview)

        // When
        val result = getTvShowReviewsUseCase(tvId)

        // Then
        assertThat(result[0].reviewText).contains("Episode 5")
    }

    @Test
    fun `getTvShowReviewsUseCase returns season-based reviews`() = runTest {
        // Given
        val tvId = 7L
        val seasonReview = sampleReviews[0].copy(
            contentTitle = "Season 2 Review",
            reviewText = "The second season improved upon the first in every way."
        )
        coEvery { tvShowRepository.getTvShowReviews(tvId) } returns listOf(seasonReview)

        // When
        val result = getTvShowReviewsUseCase(tvId)

        // Then
        assertThat(result[0].contentTitle).isEqualTo("Season 2 Review")
    }

    @Test
    fun `getTvShowReviewsUseCase makes exactly one repository call`() = runTest {
        // Given
        val tvId = 8L
        coEvery { tvShowRepository.getTvShowReviews(tvId) } returns sampleReviews

        // When
        getTvShowReviewsUseCase(tvId)

        // Then
        coVerify(exactly = 1) { tvShowRepository.getTvShowReviews(tvId) }
    }

    @Test
    fun `getTvShowReviewsUseCase returns different reviews for different TV shows`() = runTest {
        // Given
        val tvId1 = 9L
        val tvId2 = 10L
        val reviewsForShow2 = listOf(
            sampleReviews[0].copy(id = "tv_rev4", authorName = "DifferentReviewer")
        )
        coEvery { tvShowRepository.getTvShowReviews(tvId1) } returns sampleReviews
        coEvery { tvShowRepository.getTvShowReviews(tvId2) } returns reviewsForShow2

        // When
        val result1 = getTvShowReviewsUseCase(tvId1)
        val result2 = getTvShowReviewsUseCase(tvId2)

        // Then
        assertThat(result1).isNotEqualTo(result2)
        assertThat(result1.size).isGreaterThan(result2.size)
    }

    @Test
    fun `getTvShowReviewsUseCase returns reviews with various rating values`() = runTest {
        // Given
        val tvId = 11L
        val variedRatings = listOf(
            sampleReviews[0].copy(rating = 1.0),
            sampleReviews[1].copy(rating = 10.0)
        )
        coEvery { tvShowRepository.getTvShowReviews(tvId) } returns variedRatings

        // When
        val result = getTvShowReviewsUseCase(tvId)

        // Then
        assertThat(result[0].rating).isEqualTo(1.0)
        assertThat(result[1].rating).isEqualTo(10.0)
    }

    companion object {
        private lateinit var tvShowRepository: TvShowRepository
        private lateinit var getTvShowReviewsUseCase: GetTvShowReviewsUseCase

        private val sampleReviews = listOf(
            Review(
                id = "tv_rev1",
                authorName = "TVFanatic",
                authorAvatarUrl = "https://example.com/tv_avatar1.jpg",
                contentTitle = "Bingeworthy!",
                reviewText = "Couldn't stop watching this amazing series.",
                postedDate = LocalDate(2023, 9, 15),
                rating = 9.5
            ),
            Review(
                id = "tv_rev2",
                authorName = "SeriesCritic",
                authorAvatarUrl = "https://example.com/tv_avatar2.jpg",
                contentTitle = "Strong First Season",
                reviewText = "Excellent character development throughout the season.",
                postedDate = LocalDate(2023, 9, 20),
                rating = 8.5
            )
        )

        private val minimalReview = Review(
            id = "tv_rev3",
            authorName = "Anonymous",
            authorAvatarUrl = "",
            contentTitle = "",
            reviewText = "It was okay",
            postedDate = LocalDate(2023, 9, 25),
            rating = 5.0
        )
    }
}