package com.baghdad.domain.usecase.movie

import com.baghdad.domain.repository.MovieRepository
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

class GetSimilarMoviesUseCaseTest {

    private val movieRepository = mockk<MovieRepository>()
    private val getSimilarMoviesUseCase = GetSimilarMoviesUseCase(movieRepository)

    @Test
    fun `getSimilarMoviesUseCase should return similar movies when repository returns data`() =
        runTest {
            coEvery { movieRepository.getSimilarMovies(movieId) } returns similarMovies

            val result = getSimilarMoviesUseCase(movieId)

            assertThat(result).isEqualTo(similarMovies)
        }

    @Test
    fun `getSimilarMoviesUseCase should return empty list when repository returns no similar movies`() =
        runTest {
            coEvery { movieRepository.getSimilarMovies(movieId) } returns emptyList()

            val result = getSimilarMoviesUseCase(movieId)

            assertThat(result).isEmpty()
        }

    private companion object {
        val movieId = MovieMock.MOVIE.id
        val similarMovies = MovieMock.SAVED_MOVIES
    }
}