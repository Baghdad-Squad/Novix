package com.baghdad.domain.usecase.review

import com.baghdad.domain.repository.MovieRepository
import com.baghdad.domain.usecase.movie.MovieMock
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

class GetMovieReviewsUseCaseTest {

    private val movieRepository = mockk<MovieRepository>()
    private val getMovieReviewsUseCase = GetMovieReviewsUseCase(movieRepository)

    @Test
    fun `getMovieReviewsUseCase should return reviews when movie has reviews`() = runTest {
        coEvery { movieRepository.getMovieReviews(movieId) } returns reviews

        val result = getMovieReviewsUseCase(movieId)

        assertThat(result).isEqualTo(reviews)
    }

    private companion object {
        private val reviews = ReviewMock.REVIEWS
        private val movieId = MovieMock.MOVIE_ID
    }
}