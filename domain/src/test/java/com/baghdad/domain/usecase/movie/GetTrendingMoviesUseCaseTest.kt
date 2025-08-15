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
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class GetTrendingMoviesUseCaseTest {

    private lateinit var repository: MovieRepository
    private lateinit var getTrendingMoviesUseCase: GetTrendingMoviesUseCase


    @BeforeEach
    fun setUp() {
        repository = mockk()
        getTrendingMoviesUseCase = GetTrendingMoviesUseCase(repository)
    }

    @Test
    fun `invoke() should return all trending movies when genreId is null`() = runTest {
        coEvery { repository.getTrendingMovies(1) } returns sampleSavedMovies

        val result = getTrendingMoviesUseCase(1, genreId = null)

        assertThat(result).isEqualTo(sampleSavedMovies)
        coVerify { repository.getTrendingMovies(1) }
    }

    @Test
    fun `invoke() should filter trending movies by genreId`() = runTest {
        coEvery { repository.getTrendingMovies(1) } returns sampleSavedMovies

        val result = getTrendingMoviesUseCase(1, genreId = 1)

        assertThat(result).isEqualTo(sampleSavedMovies)
        coVerify { repository.getTrendingMovies(1) }
    }

    companion object{
        private val sampleSavedMovies = PagedResult(
            listOf(
                getSampleSavedMovie(),
                getSampleSavedMovie(
                    movie = getSampleMovie(
                        id = 2L,
                        genres = listOf(Genre(1L, "Action"), Genre(2L, "Drama")),
                    )
                ),
                getSampleSavedMovie(
                    movie = getSampleMovie(
                        id = 3L,
                        genres = listOf(Genre(3L, "Adventure"), Genre(5L, "Drama")),
                    )
                )
            ),
            nextPage = 2,
            prevPage = null
        )
    }
}