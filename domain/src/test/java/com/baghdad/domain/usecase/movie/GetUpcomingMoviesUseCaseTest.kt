package com.baghdad.domain.usecase.movie

import com.baghdad.domain.repository.MovieRepository
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

class GetUpcomingMoviesUseCaseTest {

    private val movieRepository = mockk<MovieRepository>()
    private val getUpcomingMoviesUseCase = GetUpcomingMoviesUseCase(movieRepository)

    @Test
    fun `getUpcomingMoviesUseCase should return upcoming movies from repository`() = runTest {
        val genreId = MovieMock.GENRE_ID
        val savedMovies = MovieMock.SAVED_MOVIES
        coEvery { movieRepository.getUpcomingMovies(genreId) } returns savedMovies

        val result = getUpcomingMoviesUseCase(genreId)

        assertThat(result).isEqualTo(savedMovies)
    }
}
