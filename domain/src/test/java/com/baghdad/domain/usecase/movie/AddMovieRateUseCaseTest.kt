package com.baghdad.domain.usecase.movie

import com.baghdad.domain.repository.MovieRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

class AddMovieRateUseCaseTest {
    private val movieRepository = mockk<MovieRepository>()
    private val addMovieRateUseCase = AddMovieRateUseCase(movieRepository)

    @Test
    fun `addMovieRate should call repository with correct movieId and rating`() = runTest {
        coEvery { movieRepository.addMovieRate(MOVIE_ID, RATING) } returns Unit

        addMovieRateUseCase(MOVIE_ID, RATING)

        coVerify(exactly = 1) { movieRepository.addMovieRate(MOVIE_ID, RATING) }
    }

    companion object {
        private const val MOVIE_ID = 123L
        private const val RATING = 5
    }
}