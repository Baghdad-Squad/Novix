package com.baghdad.domain.usecase.movie

import com.baghdad.domain.repository.MovieRepository
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class GetMovieGalleryUseCaseTest {

    @BeforeEach
    fun setUp() {
        movieRepository = mockk(relaxed = true)
        getMovieGalleryUseCase = GetMovieGalleryUseCase(movieRepository)
    }

    @Test
    fun `getMovieGalleryUseCase returns list containing poster URL`() = runTest {
        // Given
        val movieId = 1L
        coEvery { movieRepository.getMovieImages(movieId) } returns listOf("https://example.com/shawshank.jpg")

        // When
        val result = getMovieGalleryUseCase(movieId)

        // Then
        assertThat(result).hasSize(1)
        assertThat(result).containsExactly("https://example.com/shawshank.jpg")
    }

    @Test
    fun `getMovieGalleryUseCase returns empty list when no images available`() = runTest {
        // Given
        val movieId = 2L
        coEvery { movieRepository.getMovieImages(movieId) } returns emptyList()

        // When
        val result = getMovieGalleryUseCase(movieId)

        // Then
        assertThat(result).isEmpty()
    }

    @Test
    fun `getMovieGalleryUseCase returns URLs with special characters`() = runTest {
        // Given
        val movieId = 3L
        val specialUrls = listOf("https://example.com/ポスター.jpg")
        coEvery { movieRepository.getMovieImages(movieId) } returns specialUrls

        // When
        val result = getMovieGalleryUseCase(movieId)

        // Then
        assertThat(result).containsExactly("https://example.com/ポスター.jpg")
    }

    @Test
    fun `getMovieGalleryUseCase returns long URL`() = runTest {
        // Given
        val movieId = 4L
        val longUrl = "https://example.com/images/poster/with/a/very/long/path?param=value"
        coEvery { movieRepository.getMovieImages(movieId) } returns listOf(longUrl)

        // When
        val result = getMovieGalleryUseCase(movieId)

        // Then
        assertThat(result[0].length).isGreaterThan(50)
    }

    @Test
    fun `getMovieGalleryUseCase makes exactly one repository call`() = runTest {
        // Given
        val movieId = 5L
        coEvery { movieRepository.getMovieImages(movieId) } returns listOf("https://example.com/1.jpg")

        // When
        getMovieGalleryUseCase(movieId)

        // Then
        coVerify(exactly = 1) { movieRepository.getMovieImages(movieId) }
    }

    @Test
    fun `getMovieGalleryUseCase returns different images for different movies`() = runTest {
        // Given
        val movieId1 = 6L
        val movieId2 = 7L
        coEvery { movieRepository.getMovieImages(movieId1) } returns listOf("https://example.com/1.jpg")
        coEvery { movieRepository.getMovieImages(movieId2) } returns listOf("https://example.com/2.jpg")

        // When
        val result1 = getMovieGalleryUseCase(movieId1)
        val result2 = getMovieGalleryUseCase(movieId2)

        // Then
        assertThat(result1).isNotEqualTo(result2)
    }

    @Test
    fun `getMovieGalleryUseCase returns HTTPS URLs`() = runTest {
        // Given
        val movieId = 8L
        coEvery { movieRepository.getMovieImages(movieId) } returns listOf("https://secure.example.com/poster.jpg")

        // When
        val result = getMovieGalleryUseCase(movieId)

        // Then
        assertThat(result[0]).startsWith("https://")
    }

    @Test
    fun `getMovieGalleryUseCase returns URLs with query parameters`() = runTest {
        // Given
        val movieId = 9L
        val urlWithParams = "https://example.com/poster.jpg?width=500&quality=80"
        coEvery { movieRepository.getMovieImages(movieId) } returns listOf(urlWithParams)

        // When
        val result = getMovieGalleryUseCase(movieId)

        // Then
        assertThat(result[0]).contains("?width=500")
    }

    companion object {
        private lateinit var movieRepository: MovieRepository
        private lateinit var getMovieGalleryUseCase: GetMovieGalleryUseCase
    }
}
