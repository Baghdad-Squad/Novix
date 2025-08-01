package com.baghdad.domain.usecase.movie

import com.baghdad.domain.repository.MovieRepository
import com.baghdad.entity.media.Movie
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.LocalDate
import org.junit.Assert.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class GetPopularMoviesUseCaseTest {

    private lateinit var repository: MovieRepository
    private lateinit var useCase: GetPopularMoviesUseCase

    @BeforeEach
    fun setUp() {
        repository = mockk()
        useCase = GetPopularMoviesUseCase(repository)
    }

    @Test
    fun `invoke() should return popular movies from repository`() = runTest {
        // Given
        val expectedMovies = listOf(
            Movie(
                id = 1,
                title = "Inception",
                genres = emptyList(),
                averageRating = 8.8,
                userRating = null,
                releaseDate = LocalDate.parse("2010-07-16"),
                overview = "A mind-bending thriller...",
                posterImageURL = "https://image.tmdb.org/t/p/w500/inception.jpg",
                trailerURL = "https://youtube.com/inception",
                runtimeMinutes = 148
            )
        )
        coEvery { repository.getPopularMovies() } returns expectedMovies

        // When
        val result = useCase()

        // Then
        assertEquals(expectedMovies, result)
        coVerify(exactly = 1) { repository.getPopularMovies() }
    }
}
