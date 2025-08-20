package com.baghdad.domain.usecase.movie

import com.baghdad.domain.repository.MovieRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

class DeleteMovieRateUseCaseTest {
    private val movieRepository = mockk<MovieRepository>()
    private val deleteMovieRateUseCase = DeleteMovieRateUseCase(movieRepository)

    @Test
    fun `deleteMovieRate should call repository with correct movieId`() = runTest {
        coEvery { movieRepository.deleteMovieRate(MOVIE_ID) } returns Unit

        deleteMovieRateUseCase(MOVIE_ID)

        coVerify(exactly = 1) { movieRepository.deleteMovieRate(MOVIE_ID) }
    }

    companion object {
        private const val MOVIE_ID = 123L
    }
}