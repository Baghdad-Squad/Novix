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
        coEvery { movieRepository.deleteMovieRate(movieId) } returns Unit

        deleteMovieRateUseCase(movieId)

        coVerify(exactly = 1) { movieRepository.deleteMovieRate(movieId) }
    }

    private companion object {
        val movieId = MovieMock.MOVIE_ID
    }
}