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
        coEvery { movieRepository.addMovieRate(movieId, ratting) } returns Unit

        addMovieRateUseCase(movieId, ratting)

        coVerify(exactly = 1) { movieRepository.addMovieRate(movieId, ratting) }
    }

    private companion object {
        val movieId = 123L
        val ratting = 5
    }
}