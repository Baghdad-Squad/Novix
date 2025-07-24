package com.baghdad.domain.usecase.review

import com.baghdad.domain.repository.MovieRepository
import com.baghdad.entity.media.Review
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class GetMovieReviewsUseCaseTest {


    @BeforeEach
    fun setUp() {
        movieRepository = mockk(relaxed = true)
        getMovieReviewsUseCase = GetMovieReviewsUseCase(movieRepository)
    }

    @Test
    fun `getMovieReviewsUseCase returns reviews when movie has reviews`() = runTest {
        // Given
        val movieId = 1L
        coEvery { movieRepository.getMovieReviews(movieId) } returns sampleReviews

        // When
        val result = getMovieReviewsUseCase(movieId)

        // Then
        assertThat(result).hasSize(2)
        assertThat(result[0].authorName).isEqualTo("MovieFan123")
        assertThat(result[1].reviewText).contains("Solid performance")
    }

    @Test
    fun `getMovieReviewsUseCase returns empty list when movie has no reviews`() = runTest {
        // Given
        val movieId = 2L
        coEvery { movieRepository.getMovieReviews(movieId) } returns emptyList()

        // When
        val result = getMovieReviewsUseCase(movieId)

        // Then
        assertThat(result).isEmpty()
    }

    @Test
    fun `getMovieReviewsUseCase returns reviews with special characters`() = runTest {
        // Given
        val movieId = 4L
        val specialReview = sampleReviews[0].copy(
            authorName = "Critic_Élite",
            reviewText = "Fantástic performance! 10/10 would recommend."
        )
        coEvery { movieRepository.getMovieReviews(movieId) } returns listOf(specialReview)

        // When
        val result = getMovieReviewsUseCase(movieId)

        // Then
        assertThat(result[0].authorName).isEqualTo("Critic_Élite")
        assertThat(result[0].reviewText).contains("Fantástic")
    }

    @Test
    fun `getMovieReviewsUseCase returns reviews with minimal information`() = runTest {
        // Given
        val movieId = 5L
        coEvery { movieRepository.getMovieReviews(movieId) } returns listOf(minimalReview)

        // When
        val result = getMovieReviewsUseCase(movieId)

        // Then
        assertThat(result).hasSize(1)
        assertThat(result[0].authorAvatarUrl).isEmpty()
        assertThat(result[0].contentTitle).isEmpty()
    }

    @Test
    fun `getMovieReviewsUseCase returns reviews with different rating values`() = runTest {
        // Given
        val movieId = 6L
        val variedRatings = listOf(
            sampleReviews[0].copy(rating = 1f),
            sampleReviews[1].copy(rating = 10f)
        )
        coEvery { movieRepository.getMovieReviews(movieId) } returns variedRatings

        // When
        val result = getMovieReviewsUseCase(movieId)

        // Then
        assertThat(result[0].rating).isEqualTo(1f)
        assertThat(result[1].rating).isEqualTo(10f)
    }

    @Test
    fun `getMovieReviewsUseCase makes exactly one repository call`() = runTest {
        // Given
        val movieId = 7L
        coEvery { movieRepository.getMovieReviews(movieId) } returns sampleReviews

        // When
        getMovieReviewsUseCase(movieId)

        // Then
        coVerify(exactly = 1) { movieRepository.getMovieReviews(movieId) }
    }

    @Test
    fun `getMovieReviewsUseCase returns different reviews for different movies`() = runTest {
        // Given
        val movieId1 = 8L
        val movieId2 = 9L
        val reviewsForMovie2 = listOf(
            sampleReviews[0].copy(id = "rev4", authorName = "DifferentReviewer")
        )
        coEvery { movieRepository.getMovieReviews(movieId1) } returns sampleReviews
        coEvery { movieRepository.getMovieReviews(movieId2) } returns reviewsForMovie2

        // When
        val result1 = getMovieReviewsUseCase(movieId1)
        val result2 = getMovieReviewsUseCase(movieId2)

        // Then
        assertThat(result1).isNotEqualTo(result2)
        assertThat(result1.size).isGreaterThan(result2.size)
    }

    @Test
    fun `getMovieReviewsUseCase returns reviews with long text content`() = runTest {
        // Given
        val movieId = 10L
        val longReview = sampleReviews[0].copy(
            reviewText = "This is a very long review text that exceeds normal length " +
                    "requirements for testing purposes. It should test how the system handles " +
                    "reviews with extensive content that might span multiple paragraphs."
        )
        coEvery { movieRepository.getMovieReviews(movieId) } returns listOf(longReview)

        // When
        val result = getMovieReviewsUseCase(movieId)

        // Then
        assertThat(result[0].reviewText.length).isGreaterThan(100)
    }

    companion object{
        private lateinit var movieRepository: MovieRepository
        private lateinit var getMovieReviewsUseCase: GetMovieReviewsUseCase

        private val sampleReviews = listOf(
            Review(
                id = "rev1",
                authorName = "MovieFan123",
                authorAvatarUrl = "https://example.com/avatar1.jpg",
                contentTitle = "Great Movie!",
                reviewText = "Amazing plot and character development.",
                postedDate = kotlinx.datetime.LocalDate(2023, 10, 1),
                rating = 9f
            ),
            Review(
                id = "rev2",
                authorName = "CriticExpert",
                authorAvatarUrl = "https://example.com/avatar2.jpg",
                contentTitle = "Solid Performance",
                reviewText = "Solid performance by the lead actor, but the story was a bit predictable.",
                postedDate = kotlinx.datetime.LocalDate(2023, 10, 2),
                rating = 8f
            )
        )

        private val minimalReview = Review(
            id = "rev3",
            authorName = "Anon",
            authorAvatarUrl = "",
            contentTitle = "",
            reviewText = "Okay",
            postedDate = kotlinx.datetime.LocalDate(2023, 10, 3),
            rating = 5f
        )

    }
}