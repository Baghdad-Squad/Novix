package com.baghdad.domain.usecase.movie

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

class GetMoviesByGenreUseCaseTest {

    @BeforeEach
    fun setUp() {
        movieRepository = mockk(relaxed = true)
        getMoviesByGenreUseCase = GetMoviesByGenreUseCase(movieRepository)
    }

    @Test
    fun `getMoviesByGenreUseCase returns movies for specified genre`() = runTest {
        // Given
        val genreId = 1L
        val page = 1
        coEvery { movieRepository.getMoviesByGenre(genreId, page) } returns actionMovies

        // When
        val result = getMoviesByGenreUseCase(genreId, page)

        // Then
        assertThat(result).hasSize(2)
        result.forEach { movie ->
            assertThat(movie.genres.map { it.id }).contains(genreId)
        }

    }

    @Test
    fun `getMoviesByGenreUseCase returns different movies for different genres`() = runTest {
        // Given
        val actionGenreId = 1L
        val comedyGenreId = 2L
        val page = 1
        coEvery { movieRepository.getMoviesByGenre(actionGenreId, page) } returns actionMovies
        coEvery { movieRepository.getMoviesByGenre(comedyGenreId, page) } returns comedyMovies

        // When
        val actionResult = getMoviesByGenreUseCase(actionGenreId, page)
        val comedyResult = getMoviesByGenreUseCase(comedyGenreId, page)

        // Then
        assertThat(actionResult).isNotEqualTo(comedyResult)
        actionResult.forEach { movie ->
            assertThat(movie.genres.map { it.id }).contains(actionGenreId)
        }
        comedyResult.forEach { movie ->
            assertThat(movie.genres.map { it.id }).contains(comedyGenreId)
        }
    }

    @Test
    fun `getMoviesByGenreUseCase returns empty list when no movies exist for genre`() = runTest {
        // Given
        val genreId = 3L
        val page = 1
        coEvery { movieRepository.getMoviesByGenre(genreId, page) } returns emptyList()

        // When
        val result = getMoviesByGenreUseCase(genreId, page)

        // Then
        assertThat(result).isEmpty()
    }

    @Test
    fun `getMoviesByGenreUseCase returns different pages of results`() = runTest {
        // Given
        val genreId = 1L
        val page1Movies = actionMovies
        val page2Movies = listOf(
            Movie(
                id = 4L,
                title = "John Wick",
                genres = listOf(Genre(id = 1L, name = "Action")),
                overview = "An ex-hit-man comes out of retirement...",
                posterImageURL = "https://example.com/johnwick.jpg",
                averageRating = 7.4,
                releaseDate = LocalDate(2014, 10, 24),
                userRating = 7.6,
                trailerURL = "https://example.com/johnwick_trailer.mp4",
                runtimeMinutes = 101
            )
        )
        coEvery { movieRepository.getMoviesByGenre(genreId, 1) } returns page1Movies
        coEvery { movieRepository.getMoviesByGenre(genreId, 2) } returns page2Movies

        // When
        val page1Result = getMoviesByGenreUseCase(genreId, 1)
        val page2Result = getMoviesByGenreUseCase(genreId, 2)

        // Then
        assertThat(page1Result).hasSize(2)
        assertThat(page2Result).hasSize(1)
        assertThat(page1Result + page2Result).hasSize(3)
    }

    @Test
    fun `getMoviesByGenreUseCase makes exactly one repository call per invocation`() = runTest {
        // Given
        val genreId = 1L
        val page = 1
        coEvery { movieRepository.getMoviesByGenre(genreId, page) } returns actionMovies

        // When
        getMoviesByGenreUseCase(genreId, page)

        // Then
        coVerify(exactly = 1) { movieRepository.getMoviesByGenre(genreId, page) }
    }


    @Test
    fun `getMoviesByGenreUseCase returns movies with all required fields populated`() = runTest {
        // Given
        val genreId = 1L
        val page = 1
        coEvery { movieRepository.getMoviesByGenre(genreId, page) } returns actionMovies

        // When
        val result = getMoviesByGenreUseCase(genreId, page)

        // Then
        result.forEach { movie ->
            assertThat(movie.id).isNotNull()
            assertThat(movie.title).isNotEmpty()
            assertThat(movie.posterImageURL).isNotEmpty()
            assertThat(movie.genres).isNotEmpty()
        }
    }

    @Test
    fun `getMoviesByGenreUseCase returns movies with multiple genres when primary matches`() = runTest {
        // Given
        val genreId = 1L // Action
        val page = 1
        val multiGenreMovie = listOf(
            Movie(
                id = 5L,
                title = "The Matrix",
                genres = listOf(
                    Genre(id = 1L, name = "Action"),
                    Genre(id = 3L, name = "Sci-Fi")
                ),
                overview = "A computer hacker learns...",
                posterImageURL = "https://example.com/matrix.jpg",
                averageRating = 8.7,
                releaseDate = LocalDate(1999, 3, 31),
                userRating = 7.5,
                trailerURL = "https://example.com/matrix_trailer.mp4",
                runtimeMinutes = 136
            )
        )
        coEvery { movieRepository.getMoviesByGenre(genreId, page) } returns multiGenreMovie

        // When
        val result = getMoviesByGenreUseCase(genreId, page)

        // Then
        assertThat(result).hasSize(1)
        assertThat(result[0].genres).hasSize(2)
        assertThat(result[0].genres.map { it.id }).contains(genreId)
    }

    companion object{
        private lateinit var movieRepository: MovieRepository
        private lateinit var getMoviesByGenreUseCase: GetMoviesByGenreUseCase

        private val actionMovies = listOf(
            Movie(
                id = 1L,
                title = "The Dark Knight",
                genres = listOf(Genre(id = 1L, name = "Action")),
                overview = "When the menace known as the Joker...",
                posterImageURL = "https://example.com/darkknight.jpg",
                averageRating = 9.0,
                releaseDate = LocalDate(2008, 7, 18),
                userRating = 9.5,
                trailerURL = "https://example.com/trailer.mp4",
                runtimeMinutes = 152
            ),
            Movie(
                id = 2L,
                title = "Mad Max: Fury Road",
                genres = listOf(Genre(id = 1L, name = "Action")),
                overview = "In a post-apocalyptic wasteland...",
                posterImageURL = "https://example.com/madmax.jpg",
                averageRating = 8.1,
                releaseDate = LocalDate(2015, 5, 15),
                userRating = 8.3,
                trailerURL = "https://example.com/madmax_trailer.mp4",
                runtimeMinutes = 120
            )
        )

        private val comedyMovies = listOf(
            Movie(
                id = 3L,
                title = "Superbad",
                genres = listOf(Genre(id = 2L, name = "Comedy")),
                overview = "Two co-dependent high school seniors...",
                posterImageURL = "https://example.com/superbad.jpg",
                averageRating = 7.6,
                releaseDate = LocalDate(2007, 8, 17),
                userRating = 7.8,
                trailerURL = "https://example.com/superbad_trailer.mp4",
                runtimeMinutes = 113
            )
        )
    }
}