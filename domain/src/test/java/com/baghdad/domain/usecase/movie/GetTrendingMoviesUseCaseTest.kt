package com.baghdad.domain.usecase.movie

import com.baghdad.domain.model.PagedResult
import com.baghdad.domain.repository.MovieRepository
import com.baghdad.entity.media.Genre
import com.baghdad.entity.media.Movie
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.LocalDate
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class GetTrendingMoviesUseCaseTest {

    private lateinit var repository: MovieRepository
    private lateinit var useCase: GetTrendingMoviesUseCase

    @BeforeEach
    fun setUp() {
        repository = mockk()
        useCase = GetTrendingMoviesUseCase(repository)
    }

    private fun createMovie(id: Long, genreIds: List<Long>): Movie {
        return Movie(
            id = id,
            title = "Movie $id",
            genres = genreIds.map { Genre(it, "Genre $it") },
            averageRating = 8.0,
            userRating = null,
            releaseDate = LocalDate.parse("2020-01-01"),
            overview = "Overview",
            posterImageURL = "poster.jpg",
            trailerURL = "trailer.mp4",
            runtimeMinutes = 120
        )
    }

    @Test
    fun `invoke() should return all trending movies when genreId is null`() = runTest {
        // Given
        val movies = listOf(createMovie(1, listOf(1, 2)), createMovie(2, listOf(3)))
        val expectedResult = PagedResult(
            data = movies,
            nextKey = 2,
            prevKey = null
        )

        coEvery { repository.getTrendingMovies(1) } returns expectedResult

        // When
        val result = useCase(1, genreId = null)

        // Then
        assertThat(result).isEqualTo(expectedResult)
        coVerify { repository.getTrendingMovies(1) }
    }

    @Test
    fun `invoke() should filter trending movies by genreId`() = runTest {
        // Given
        val movies = listOf(
            createMovie(1, listOf(1, 2)),
            createMovie(2, listOf(3)),
            createMovie(3, listOf(1))
        )
        val expectedResult = PagedResult(
            data = listOf(movies[0], movies[2]),
            nextKey = 2,
            prevKey = null
        )

        coEvery { repository.getTrendingMovies(1) } returns PagedResult(
            data = movies,
            nextKey = 2,
            prevKey = null
        )

        // When
        val result = useCase(1, genreId = 1)

        // Then
        assertThat(result).isEqualTo(expectedResult)
        coVerify { repository.getTrendingMovies(1) }
    }
}