package com.baghdad.domain.usecase.movie

import com.baghdad.domain.repository.MovieRepository
import com.baghdad.entity.media.Movie
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.LocalDate
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class GetMovieGalleryUseCaseTest {

    @BeforeEach
    fun setUp() {
        movieRepository = mockk(relaxed = true)
        getMovieGalleryUseCase = GetMovieGalleryUseCase(movieRepository)
    }

    @Test
    fun `getMovieGalleryUseCase returns list containing poster URL for standard movie`() = runTest {
        // Given
        val movieId = 1L
        coEvery { movieRepository.getMovieDetails(movieId) } returns sampleMovie

        // When
        val result = getMovieGalleryUseCase(movieId)

        // Then
        assertThat(result).hasSize(1)
        assertThat(result).containsExactly("https://example.com/shawshank.jpg")
    }

    @Test
    fun `getMovieGalleryUseCase returns empty string in list when no poster available`() = runTest {
        // Given
        val movieId = 2L
        coEvery { movieRepository.getMovieDetails(movieId) } returns minimalMovie

        // When
        val result = getMovieGalleryUseCase(movieId)

        // Then
        assertThat(result).hasSize(1)
        assertThat(result[0]).isEmpty()
    }

    @Test
    fun `getMovieGalleryUseCase returns list with special character URLs`() = runTest {
        // Given
        val movieId = 4L
        val specialCharMovie = sampleMovie.copy(
            id = movieId,
            posterImageURL = "https://example.com/movies/戦争と平和.jpg"
        )
        coEvery { movieRepository.getMovieDetails(movieId) } returns specialCharMovie

        // When
        val result = getMovieGalleryUseCase(movieId)

        // Then
        assertThat(result[0]).isEqualTo("https://example.com/movies/戦争と平和.jpg")
    }

    @Test
    fun `getMovieGalleryUseCase returns list with long URL`() = runTest {
        // Given
        val movieId = 5L
        val longUrlMovie = sampleMovie.copy(
            id = movieId,
            posterImageURL = "https://example.com/very/long/path/to/the/movie/poster/image" +
                    "/with/many/subdirectories/and/parameters?query=123&param=abc"
        )
        coEvery { movieRepository.getMovieDetails(movieId) } returns longUrlMovie

        // When
        val result = getMovieGalleryUseCase(movieId)

        // Then
        assertThat(result[0].length).isGreaterThan(50)
    }

    @Test
    fun `getMovieGalleryUseCase makes exactly one repository call`() = runTest {
        // Given
        val movieId = 1L
        coEvery { movieRepository.getMovieDetails(movieId) } returns sampleMovie

        // When
        getMovieGalleryUseCase(movieId)

        // Then
        coVerify(exactly = 1) { movieRepository.getMovieDetails(movieId) }
    }

    @Test
    fun `getMovieGalleryUseCase returns different URLs for different movies`() = runTest {
        // Given
        val movieId1 = 1L
        val movieId2 = 6L
        val anotherMovie = sampleMovie.copy(
            id = movieId2,
            posterImageURL = "https://example.com/another.jpg"
        )
        coEvery { movieRepository.getMovieDetails(movieId1) } returns sampleMovie
        coEvery { movieRepository.getMovieDetails(movieId2) } returns anotherMovie

        // When
        val result1 = getMovieGalleryUseCase(movieId1)
        val result2 = getMovieGalleryUseCase(movieId2)

        // Then
        assertThat(result1).isNotEqualTo(result2)
        assertThat(result1[0]).endsWith("shawshank.jpg")
        assertThat(result2[0]).endsWith("another.jpg")
    }

    @Test
    fun `getMovieGalleryUseCase returns secure HTTPS URLs`() = runTest {
        // Given
        val movieId = 7L
        val httpsMovie = sampleMovie.copy(
            id = movieId,
            posterImageURL = "https://secure.example.com/poster.jpg"
        )
        coEvery { movieRepository.getMovieDetails(movieId) } returns httpsMovie

        // When
        val result = getMovieGalleryUseCase(movieId)

        // Then
        assertThat(result[0]).startsWith("https://")
    }

    @Test
    fun `getMovieGalleryUseCase returns URL with query parameters when present`() = runTest {
        // Given
        val movieId = 8L
        val parameterizedMovie = sampleMovie.copy(
            id = movieId,
            posterImageURL = "https://example.com/poster.jpg?width=500&quality=80"
        )
        coEvery { movieRepository.getMovieDetails(movieId) } returns parameterizedMovie

        // When
        val result = getMovieGalleryUseCase(movieId)

        // Then
        assertThat(result[0]).contains("?width=500")
    }

    companion object{
        private lateinit var movieRepository: MovieRepository
        private lateinit var getMovieGalleryUseCase: GetMovieGalleryUseCase

        private val sampleMovie = Movie(
            id = 1L,
            title = "The Shawshank Redemption",
            posterImageURL = "https://example.com/shawshank.jpg",
            overview = "Two imprisoned men bond...",
            averageRating = 9.3,
            releaseDate = LocalDate(1994, 9, 23),
            genres = emptyList(),
            userRating = 9.3,
            trailerURL = "https://example.com/trailer.mp4",
            runtimeMinutes = 142,
        )

        private val minimalMovie = Movie(
            id = 2L,
            title = "Minimal Movie",
            posterImageURL = "",
            overview = "",
            averageRating = 0.0,
            releaseDate = LocalDate(2020, 1, 1),
            genres = emptyList(),
            userRating = 0.0,
            trailerURL = "",
            runtimeMinutes = 0,
        )
    }
}