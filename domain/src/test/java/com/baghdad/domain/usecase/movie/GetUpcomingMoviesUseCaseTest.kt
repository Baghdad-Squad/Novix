package com.baghdad.domain.usecase.movie

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


class GetUpcomingMoviesUseCaseTest {

    private lateinit var movieRepository: MovieRepository
    private lateinit var useCase: GetUpcomingMoviesUseCase

    @BeforeEach
    fun setUp() {
        movieRepository = mockk()
        useCase = GetUpcomingMoviesUseCase(movieRepository)
    }

    private fun createMovie(id: Long): Movie {
        return Movie(
            id = id,
            title = "Movie $id",
            genres = listOf(Genre(1, "Action")),
            averageRating = 7.5,
            userRating = null,
            releaseDate = LocalDate.parse("2025-09-01"),
            overview = "Upcoming movie overview",
            posterImageURL = "poster.jpg",
            trailerURL = "trailer.mp4",
            runtimeMinutes = 110
        )
    }

    @Test
    fun `invoke should return upcoming movies from repository`() = runTest {
        // Given
        val genreId = 1L
        val movies = listOf(createMovie(1), createMovie(2))
        coEvery { movieRepository.getUpcomingMovies(genreId) } returns movies

        // When
        val result = useCase(genreId)

        // Then
        assertEquals(movies, result)
        coVerify(exactly = 1) { movieRepository.getUpcomingMovies(genreId) }
    }
}
