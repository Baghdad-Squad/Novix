package com.baghdad.domain.usecase.tvShow

import com.baghdad.domain.repository.TvShowRepository
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class GetTvShowImagesUseCaseTest {

    @BeforeEach
    fun setUp() {
        tvShowRepository = mockk(relaxed = true)
        getTvShowImagesUseCase = GetTvShowImagesUseCase(tvShowRepository)
    }

    @Test
    fun `getTvShowImagesUseCase returns list of image URLs`() = runTest {
        // Given
        val tvId = 1L
        val expectedUrls = listOf(
            "https://example.com/image1.jpg",
            "https://example.com/image2.jpg",
            "https://example.com/image3.jpg"
        )
        coEvery { tvShowRepository.getTvShowImages(tvId) } returns expectedUrls

        // When
        val result = getTvShowImagesUseCase(tvId)

        // Then
        assertThat(result).hasSize(3)
        assertThat(result).isEqualTo(expectedUrls)
    }

    @Test
    fun `getTvShowImagesUseCase returns empty list when no images available`() = runTest {
        // Given
        val tvId = 2L
        coEvery { tvShowRepository.getTvShowImages(tvId) } returns emptyList()

        // When
        val result = getTvShowImagesUseCase(tvId)

        // Then
        assertThat(result).isEmpty()
    }

    @Test
    fun `getTvShowImagesUseCase returns list with special character URLs`() = runTest {
        // Given
        val tvId = 3L
        val expectedUrls = listOf("https://example.com/tv/戦争と平和.jpg")
        coEvery { tvShowRepository.getTvShowImages(tvId) } returns expectedUrls

        // When
        val result = getTvShowImagesUseCase(tvId)

        // Then
        assertThat(result[0]).isEqualTo("https://example.com/tv/戦争と平和.jpg")
    }

    @Test
    fun `getTvShowImagesUseCase returns list with long URLs`() = runTest {
        // Given
        val tvId = 4L
        val longUrl = "https://example.com/very/long/path/to/the/tv/show/images" +
                "/with/many/subdirectories/and/parameters?query=123&param=abc"
        coEvery { tvShowRepository.getTvShowImages(tvId) } returns listOf(longUrl)

        // When
        val result = getTvShowImagesUseCase(tvId)

        // Then
        assertThat(result[0].length).isGreaterThan(50)
    }

    @Test
    fun `getTvShowImagesUseCase makes exactly one repository call`() = runTest {
        // Given
        val tvId = 1L
        coEvery { tvShowRepository.getTvShowImages(tvId) } returns emptyList()

        // When
        getTvShowImagesUseCase(tvId)

        // Then
        coVerify(exactly = 1) { tvShowRepository.getTvShowImages(tvId) }
    }

    @Test
    fun `getTvShowImagesUseCase returns different URLs for different TV shows`() = runTest {
        // Given
        val tvId1 = 1L
        val tvId2 = 2L
        val urls1 = listOf("https://example.com/show1.jpg")
        val urls2 = listOf("https://example.com/show2.jpg")
        coEvery { tvShowRepository.getTvShowImages(tvId1) } returns urls1
        coEvery { tvShowRepository.getTvShowImages(tvId2) } returns urls2

        // When
        val result1 = getTvShowImagesUseCase(tvId1)
        val result2 = getTvShowImagesUseCase(tvId2)

        // Then
        assertThat(result1).isNotEqualTo(result2)
        assertThat(result1[0]).endsWith("show1.jpg")
        assertThat(result2[0]).endsWith("show2.jpg")
    }

    @Test
    fun `getTvShowImagesUseCase returns secure HTTPS URLs`() = runTest {
        // Given
        val tvId = 5L
        val expectedUrls = listOf("https://secure.example.com/image.jpg")
        coEvery { tvShowRepository.getTvShowImages(tvId) } returns expectedUrls

        // When
        val result = getTvShowImagesUseCase(tvId)

        // Then
        assertThat(result[0]).startsWith("https://")
    }

    @Test
    fun `getTvShowImagesUseCase returns URLs with query parameters when present`() = runTest {
        // Given
        val tvId = 6L
        val expectedUrls = listOf("https://example.com/image.jpg?width=500&quality=80")
        coEvery { tvShowRepository.getTvShowImages(tvId) } returns expectedUrls

        // When
        val result = getTvShowImagesUseCase(tvId)

        // Then
        assertThat(result[0]).contains("?width=500")
    }

    @Test
    fun `getTvShowImagesUseCase returns multiple image URLs`() = runTest {
        // Given
        val tvId = 7L
        val expectedUrls = listOf(
            "https://example.com/image1.jpg",
            "https://example.com/image2.jpg",
            "https://example.com/image3.jpg",
            "https://example.com/image4.jpg"
        )
        coEvery { tvShowRepository.getTvShowImages(tvId) } returns expectedUrls

        // When
        val result = getTvShowImagesUseCase(tvId)

        // Then
        assertThat(result).hasSize(4)
        assertThat(result).isEqualTo(expectedUrls)
    }

    companion object {
        private lateinit var tvShowRepository: TvShowRepository
        private lateinit var getTvShowImagesUseCase: GetTvShowImagesUseCase
    }
}