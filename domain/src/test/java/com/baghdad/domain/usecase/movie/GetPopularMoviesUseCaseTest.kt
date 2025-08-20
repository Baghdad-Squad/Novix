package com.baghdad.domain.usecase.movie

import com.baghdad.domain.repository.MovieRepository
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

class GetPopularMoviesUseCaseTest {

    private val repository = mockk<MovieRepository>()
    private val useCase: GetPopularMoviesUseCase = GetPopularMoviesUseCase(repository)

    @Test
    fun `getPopularMoviesUseCase should return movies when repository succeeds`() = runTest {
        coEvery { repository.getPopularMovies() } returns savedMovies

        val result = useCase.invoke()

        assertThat(result).isEqualTo(savedMovies)
    }

    private companion object {
        val movie = MovieMock.MOVIE
        val savedMovies = MovieMock.SAVED_MOVIES
    }
}