package com.baghdad.domain.usecase.movie

import com.baghdad.domain.repository.MovieRepository
import com.baghdad.entity.media.Genre
import com.baghdad.entity.media.Movie
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.LocalDate
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow

class GetSimilarMoviesUseCaseTest {


    @BeforeEach
    fun setup() {
        movieRepository = mockk(relaxed = true)
        getSimilarMoviesUseCase = GetSimilarMoviesUseCase(movieRepository)
    }

    @Test
    fun `invoke() should return similar movies when repository returns data`() = runTest {
        val movieId = 1L
        coEvery { movieRepository.getSimilarMovies(movieId) } returns similarMovies
        val result = getSimilarMoviesUseCase(movieId)
        assertThat(result).isEqualTo(similarMovies)
    }

    @Test
    fun `invoke() should return empty list when repository returns no similar movies`() = runTest {
        val movieId = 1L
        coEvery { movieRepository.getSimilarMovies(movieId) } returns emptyList()
        val result = getSimilarMoviesUseCase(movieId)
        assertThat(result).isEmpty()
    }

    @Test
    fun `invoke() should not throw exception when called successfully`() = runTest {
        val movieId = 1L
        coEvery { movieRepository.getSimilarMovies(movieId) } returns similarMovies
        assertDoesNotThrow {
            getSimilarMoviesUseCase.invoke(movieId)
        }
    }

    private companion object {

        lateinit var getSimilarMoviesUseCase: GetSimilarMoviesUseCase
        lateinit var movieRepository: MovieRepository

        val similarMovies = listOf(
            Movie(
                id = 1L,
                title = "The Dark Knight",
                genres = listOf(Genre(28, "Action"), Genre(80, "Crime"), Genre(18, "Drama")),
                averageRating = 9.0,
                userRating = 8.5,
                releaseDate = LocalDate(2008, 7, 18),
                overview = "Batman faces the Joker in a battle for Gotham's soul.",
                posterImageURL = "https://example.com/posters/dark_knight.jpg",
                runtimeMinutes = 152,
                trailerURL = ""
            ),
            Movie(
                id = 2L,
                title = "Inception",
                genres = listOf(Genre(28, "Action"), Genre(878, "Sci-Fi"), Genre(53, "Thriller")),
                averageRating = 8.8,
                userRating = 9.0,
                releaseDate = LocalDate(2010, 7, 16),
                overview = "A thief enters dreams to steal secrets, but gets caught in a deeper plan.",
                posterImageURL = "https://example.com/posters/inception.jpg",
                runtimeMinutes = 148,
                trailerURL = ""
            ),
            Movie(
                id = 3L,
                title = "Interstellar",
                genres = listOf(Genre(12, "Adventure"), Genre(18, "Drama"), Genre(878, "Sci-Fi")),
                averageRating = 8.6,
                userRating = 9.2,
                releaseDate = LocalDate(2014, 11, 7),
                overview = "A group of explorers travel through a wormhole to save humanity.",
                posterImageURL = "https://example.com/posters/interstellar.jpg",
                runtimeMinutes = 169,
                trailerURL = ""
            )
        )
    }
}

