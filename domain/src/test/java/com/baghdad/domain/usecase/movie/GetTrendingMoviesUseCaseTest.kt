package com.baghdad.domain.usecase.movie

import com.baghdad.domain.model.pagination.PagedResult
import com.baghdad.domain.repository.MovieRepository
import com.baghdad.domain.testHelper.getSampleMovie
import com.baghdad.domain.testHelper.getSampleSavedMovie
import com.baghdad.entity.media.Genre
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

class GetTrendingMoviesUseCaseTest {

    private val repository = mockk<MovieRepository>()
    private val getTrendingMoviesUseCase = GetTrendingMoviesUseCase(repository)

    @Test
    fun `getTrendingMoviesUseCase should return all trending movies when genreId is null`() =
        runTest {
            coEvery { repository.getTrendingMovies(1) } returns sampleSavedMovies

            val result = getTrendingMoviesUseCase(1, genreId = null)

            assertThat(result).isEqualTo(sampleSavedMovies)
            coVerify { repository.getTrendingMovies(1) }
        }

    @Test
    fun `getTrendingMoviesUseCase should filter trending movies by genreId`() = runTest {
        coEvery { repository.getTrendingMovies(1) } returns sampleSavedMovies

        val result = getTrendingMoviesUseCase(page = 1, genreId = 1)

        assertThat(result.data).containsExactly(
            getSampleSavedMovie(movie = getSampleMovie(id = 2L, genres = genres))
        )
    }

    @Test
    fun `getTrendingMoviesUseCase should return empty list when no movie matches genreId`() =
        runTest {
            coEvery { repository.getTrendingMovies(1) } returns sampleSavedMovies

            val result = getTrendingMoviesUseCase(page = 1, genreId = 999L)

            assertThat(result.data).isEmpty()
        }

    companion object {
        val genre = Genre(1L, "Action")
        val genres = listOf(genre, genre.copy(id = 2L, name = "Drama"))

        val sampleSavedMovies = PagedResult(
            data = listOf(
                getSampleSavedMovie(),
                getSampleSavedMovie(movie = getSampleMovie(id = 2L, genres = genres)),
            ),
            nextPage = 2,
            prevPage = null
        )
    }
}