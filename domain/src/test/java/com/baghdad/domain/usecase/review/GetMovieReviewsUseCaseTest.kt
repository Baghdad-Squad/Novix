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
    private lateinit var movieRepository: MovieRepository
    private lateinit var getMovieReviewsUseCase: GetMovieReviewsUseCase

    @BeforeEach
    fun setUp() {
        movieRepository = mockk(relaxed = true)
        getMovieReviewsUseCase = GetMovieReviewsUseCase(movieRepository)
    }

    @Test
    fun `invoke() should return reviews when movie has reviews`() = runTest {
        val movieId = 1L
        coEvery { movieRepository.getMovieReviews(movieId) } returns sampleReviews

        val result = getMovieReviewsUseCase(movieId)

        assertThat(result).hasSize(2)
        assertThat(result[0].authorName).isEqualTo("MovieFan123")
        assertThat(result[1].reviewText).contains("Solid performance")
    }

    @Test
    fun `invoke() should return empty list when movie has no reviews`() = runTest {
        val movieId = 2L
        coEvery { movieRepository.getMovieReviews(movieId) } returns emptyList()

        val result = getMovieReviewsUseCase(movieId)

        assertThat(result).isEmpty()
    }

    @Test
    fun `invoke() should return reviews with special characters`() = runTest {
        val movieId = 4L
        val specialReview = sampleReviews[0].copy(
            authorName = "Critic_Élite",
            reviewText = "Fantástic performance! 10/10 would recommend."
        )
        coEvery { movieRepository.getMovieReviews(movieId) } returns listOf(specialReview)

        val result = getMovieReviewsUseCase(movieId)

        assertThat(result[0].authorName).isEqualTo("Critic_Élite")
        assertThat(result[0].reviewText).contains("Fantástic")
    }

    @Test
    fun `invoke() should return reviews with minimal information`() = runTest {
        val movieId = 5L
        coEvery { movieRepository.getMovieReviews(movieId) } returns listOf(minimalReview)

        val result = getMovieReviewsUseCase(movieId)

        assertThat(result).hasSize(1)
        assertThat(result[0].authorAvatarUrl).isEmpty()
        assertThat(result[0].authorUsername).isEmpty()
    }

    @Test
    fun `invoke() should return reviews with different rating values`() = runTest {
        val movieId = 6L
        val variedRatings = listOf(
            sampleReviews[0].copy(rating = 1.0),
            sampleReviews[1].copy(rating = 10.0)
        )
        coEvery { movieRepository.getMovieReviews(movieId) } returns variedRatings

        val result = getMovieReviewsUseCase(movieId)

        assertThat(result[0].rating).isEqualTo(1)
        assertThat(result[1].rating).isEqualTo(10)
    }

    @Test
    fun `invoke() should make exactly one repository call`() = runTest {
        val movieId = 7L
        coEvery { movieRepository.getMovieReviews(movieId) } returns sampleReviews

        getMovieReviewsUseCase(movieId)

        coVerify(exactly = 1) { movieRepository.getMovieReviews(movieId) }
    }

    @Test
    fun `invoke() should return different reviews for different movies`() = runTest {
        val movieId1 = 8L
        val movieId2 = 9L
        val reviewsForMovie2 = listOf(
            sampleReviews[0].copy(id = "rev4", authorName = "DifferentReviewer")
        )
        coEvery { movieRepository.getMovieReviews(movieId1) } returns sampleReviews
        coEvery { movieRepository.getMovieReviews(movieId2) } returns reviewsForMovie2

        val result1 = getMovieReviewsUseCase(movieId1)
        val result2 = getMovieReviewsUseCase(movieId2)

        assertThat(result1).isNotEqualTo(result2)
        assertThat(result1.size).isGreaterThan(result2.size)
    }

    @Test
    fun `invoke() should return reviews with long text content`() = runTest {
        val movieId = 10L
        val longReview = sampleReviews[0].copy(
            reviewText = "This is a very long review text that exceeds normal length " +
                    "requirements for testing purposes. It should test how the system handles " +
                    "reviews with extensive content that might span multiple paragraphs."
        )
        coEvery { movieRepository.getMovieReviews(movieId) } returns listOf(longReview)

        val result = getMovieReviewsUseCase(movieId)

        assertThat(result[0].reviewText.length).isGreaterThan(100)
    }

    companion object {

        private val sampleReviews = listOf(
            Review(
                id = "rev1",
                authorName = "MovieFan123",
                authorAvatarUrl = "https://example.com/avatar1.jpg",
                authorUsername = "Great Movie!",
                reviewText = "Amazing plot and character development.",
                postedDate = kotlinx.datetime.LocalDate(2023, 10, 1),
                rating = 9.0
            ),
            Review(
                id = "rev2",
                authorName = "CriticExpert",
                authorAvatarUrl = "https://example.com/avatar2.jpg",
                authorUsername = "Solid Performance",
                reviewText = "Solid performance by the lead actor, but the story was a bit predictable.",
                postedDate = kotlinx.datetime.LocalDate(2023, 10, 2),
                rating = 8.0
            )
        )

        private val minimalReview = Review(
            id = "rev3",
            authorName = "Anon",
            authorAvatarUrl = "",
            authorUsername = "",
            reviewText = "Okay",
            postedDate = kotlinx.datetime.LocalDate(2023, 10, 3),
            rating = 5.0
        )
    }
}