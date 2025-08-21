package com.baghdad.domain.usecase.movie

import com.baghdad.domain.repository.MovieRepository
import com.baghdad.domain.usecase.genre.GenreMock
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

class GetTrendingMoviesUseCaseTest {

    private val repository = mockk<MovieRepository>()
    private val getTrendingMoviesUseCase = GetTrendingMoviesUseCase(repository)

    @Test
    fun `getTrendingMoviesUseCase should return all trending movies when genreId is null`() =
        runTest {
            coEvery { repository.getTrendingMovies(page = page) } returns savedMovie

            val result = getTrendingMoviesUseCase(page = page, genreId = null)

            assertThat(result).isEqualTo(savedMovie)
        }

    @Test
    fun `getTrendingMoviesUseCase should return filtered trending movies when genreId is provided`() =
        runTest {
            coEvery { repository.getTrendingMovies(page = page) } returns savedMovie

            val result = getTrendingMoviesUseCase(page = page, genreId = genreId)

            assertThat(result).isEqualTo(savedMovie)
        }

    private companion object {
        val page = MovieMock.PAGE
        val savedMovie = MovieMock.SAVED_MOVIES_PAGED_RESULT
        val genreId = GenreMock.GENRE_ID
    }
}