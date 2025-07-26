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

class GetMovieCategoryUseCaseTest {

    @BeforeEach
    fun setUp() {
        movieRepository = mockk(relaxed = true)
        getMovieCategoryUseCase = GetMovieCategoryUseCase(movieRepository)
    }

    @Test
    fun `getMovieCategoryUseCase returns genres for movie with multiple categories`() = runTest {
        // Given
        val movieId = 1L
        coEvery { movieRepository.getMovieDetails(movieId) } returns sampleMovie

        // When
        val result = getMovieCategoryUseCase(movieId)

        // Then
        assertThat(result).hasSize(3)
        assertThat(result).containsExactly(
            Genre(id = 1L, name = "Sci-Fi"),
            Genre(id = 2L, name = "Action"),
            Genre(id = 3L, name = "Thriller")
        )
    }

    @Test
    fun `getMovieCategoryUseCase returns empty list for movie with no genres`() = runTest {
        // Given
        val movieId = 2L
        coEvery { movieRepository.getMovieDetails(movieId) } returns minimalMovie

        // When
        val result = getMovieCategoryUseCase(movieId)

        // Then
        assertThat(result).isEmpty()
    }

    @Test
    fun `getMovieCategoryUseCase returns single genre for movie with one category`() = runTest {
        // Given
        val movieId = 3L
        val singleGenreMovie = sampleMovie.copy(
            id = movieId,
            genres = listOf(Genre(id = 4L, name = "Drama"))
        )
        coEvery { movieRepository.getMovieDetails(movieId) } returns singleGenreMovie

        // When
        val result = getMovieCategoryUseCase(movieId)

        // Then
        assertThat(result).hasSize(1)
        assertThat(result[0].name).isEqualTo("Drama")
    }

    @Test
    fun `getMovieCategoryUseCase returns genres with special characters`() = runTest {
        // Given
        val movieId = 4L
        val specialGenreMovie = sampleMovie.copy(
            id = movieId,
            genres = listOf(
                Genre(id = 5L, name = "Sci-Fi/Fantasy"),
                Genre(id = 6L, name = "Action-Adventure")
            )
        )
        coEvery { movieRepository.getMovieDetails(movieId) } returns specialGenreMovie

        // When
        val result = getMovieCategoryUseCase(movieId)

        // Then
        assertThat(result[0].name).isEqualTo("Sci-Fi/Fantasy")
        assertThat(result[1].name).isEqualTo("Action-Adventure")
    }

    @Test
    fun `getMovieCategoryUseCase returns genres with unicode characters`() = runTest {
        // Given
        val movieId = 5L
        val unicodeGenreMovie = sampleMovie.copy(
            id = movieId,
            genres = listOf(Genre(id = 7L, name = "Dramédie"))
        )
        coEvery { movieRepository.getMovieDetails(movieId) } returns unicodeGenreMovie

        // When
        val result = getMovieCategoryUseCase(movieId)

        // Then
        assertThat(result[0].name).isEqualTo("Dramédie")
    }

    @Test
    fun `getMovieCategoryUseCase makes exactly one repository call`() = runTest {
        // Given
        val movieId = 1L
        coEvery { movieRepository.getMovieDetails(movieId) } returns sampleMovie

        // When
        getMovieCategoryUseCase(movieId)

        // Then
        coVerify(exactly = 1) { movieRepository.getMovieDetails(movieId) }
    }

    @Test
    fun `getMovieCategoryUseCase returns different genres for different movies`() = runTest {
        // Given
        val movieId1 = 1L
        val movieId2 = 6L
        val comedyMovie = sampleMovie.copy(
            id = movieId2,
            genres = listOf(Genre(id = 8L, name = "Comedy"))
        )
        coEvery { movieRepository.getMovieDetails(movieId1) } returns sampleMovie
        coEvery { movieRepository.getMovieDetails(movieId2) } returns comedyMovie

        // When
        val result1 = getMovieCategoryUseCase(movieId1)
        val result2 = getMovieCategoryUseCase(movieId2)

        // Then
        assertThat(result1).isNotEqualTo(result2)
        assertThat(result1.any { it.name == "Sci-Fi" }).isTrue()
        assertThat(result2.any { it.name == "Comedy" }).isTrue()
    }

    @Test
    fun `getMovieCategoryUseCase returns genres in original order`() = runTest {
        // Given
        val movieId = 7L
        val orderedGenresMovie = sampleMovie.copy(
            id = movieId,
            genres = listOf(
                Genre(id = 9L, name = "Primary"),
                Genre(id = 10L, name = "Secondary"),
                Genre(id = 11L, name = "Tertiary")
            )
        )
        coEvery { movieRepository.getMovieDetails(movieId) } returns orderedGenresMovie

        // When
        val result = getMovieCategoryUseCase(movieId)

        // Then
        assertThat(result[0].name).isEqualTo("Primary")
        assertThat(result[1].name).isEqualTo("Secondary")
        assertThat(result[2].name).isEqualTo("Tertiary")
    }

    companion object{
        private lateinit var movieRepository: MovieRepository
        private lateinit var getMovieCategoryUseCase: GetMovieCategoryUseCase

        private val sampleMovie = Movie(
            id = 1L,
            title = "Inception",
            genres = listOf(
                Genre(id = 1L, name = "Sci-Fi"),
                Genre(id = 2L, name = "Action"),
                Genre(id = 3L, name = "Thriller")
            ),

            overview = "A thief who steals corporate secrets...",
            posterImageURL = "https://example.com/poster.jpg",
            averageRating = 8.8,
            releaseDate = LocalDate(2010, 7, 16),
            userRating = 9.0,
            trailerURL = "https://example.com/trailer.mp4",
            runtimeMinutes = 148,
        )

        private val minimalMovie = Movie(
            id = 2L,
            title = "Minimal Movie",
            genres = emptyList(),
            overview = "",
            posterImageURL = "",
            averageRating = 0.0,
            releaseDate = LocalDate(2020, 1, 1),
            userRating = 0.0,
            trailerURL = "",
            runtimeMinutes = 0,
        )
    }
}