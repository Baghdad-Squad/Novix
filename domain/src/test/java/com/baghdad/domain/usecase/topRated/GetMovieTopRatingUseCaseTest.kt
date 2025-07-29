package com.baghdad.domain.usecase.topRated

import com.baghdad.domain.model.PagedResult
import com.baghdad.domain.repository.MovieRepository
import com.baghdad.entity.media.Genre
import com.baghdad.entity.media.Movie
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.LocalDate
import org.junit.Assert.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class GetMovieTopRatingUseCaseTest {

    private lateinit var movieRepository: MovieRepository
    private lateinit var useCase: GetMovieTopRatingUseCase

    @BeforeEach
    fun setUp() {
        movieRepository = mockk()
        useCase = GetMovieTopRatingUseCase(movieRepository)
    }

    private fun createMovie(id: Long, genreIds: List<Long>): Movie {
        val genres = genreIds.map { Genre(id = it, name = "Genre $it") }
        return Movie(
            id = id,
            title = "Movie $id",
            genres = genres,
            averageRating = 8.5,
            userRating = null,
            releaseDate = LocalDate.parse("2024-01-01"),
            overview = "Overview of movie $id",
            posterImageURL = "https://example.com/movie$id.jpg",
            trailerURL = "https://example.com/trailer$id.mp4",
            runtimeMinutes = 120
        )
    }

    @Test
    fun `invoke should return all movies when genreId is null`() = runTest {
        // Given
        val page = 1
        val movies = listOf(
            createMovie(1, listOf(1)),
            createMovie(2, listOf(2))
        )
        val pagedResult = PagedResult(data = movies, nextKey = 2, prevKey = null)

        coEvery { movieRepository.getTopRatedMovies(page) } returns pagedResult

        // When
        val result = useCase(page, genreId = null)

        // Then
        assertEquals(movies, result.data)
        coVerify(exactly = 1) { movieRepository.getTopRatedMovies(page) }
    }

    @Test
    fun `invoke should return only movies matching genreId`() = runTest {
        // Given
        val page = 1
        val genreId = 2L
        val movies = listOf(
            createMovie(1, listOf(1)),
            createMovie(2, listOf(2)),
            createMovie(3, listOf(2, 3))
        )
        val expected = listOf(movies[1], movies[2])
        val pagedResult = PagedResult(data = movies, nextKey = 2, prevKey = null)

        coEvery { movieRepository.getTopRatedMovies(page) } returns pagedResult

        // When
        val result = useCase(page, genreId)

        // Then
        assertEquals(expected, result.data)
        coVerify { movieRepository.getTopRatedMovies(page) }
    }
}
