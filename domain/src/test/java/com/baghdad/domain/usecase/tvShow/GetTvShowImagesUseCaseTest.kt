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

    private lateinit var tvShowRepository: TvShowRepository
    private lateinit var getTvShowImagesUseCase: GetTvShowImagesUseCase

    @BeforeEach
    fun setUp() {
        tvShowRepository = mockk(relaxed = true)
        getTvShowImagesUseCase = GetTvShowImagesUseCase(tvShowRepository)
    }

    @Test
    fun `getTvShowImagesUseCase() should return image URLs when repository returns data`() = runTest {
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
        assertThat(result).isEqualTo(expectedUrls)
        assertThat(result).hasSize(3)
    }

    @Test
    fun `getTvShowImagesUseCase() should return empty list when repository returns no images`() = runTest {
        // Given
        val tvId = 2L
        coEvery { tvShowRepository.getTvShowImages(tvId) } returns emptyList()

        // When
        val result = getTvShowImagesUseCase(tvId)

        // Then
        assertThat(result).isEmpty()
    }

    @Test
    fun `getTvShowImagesUseCase() should handle URLs with special characters when repository returns them`() = runTest {
        // Given
        val tvId = 3L
        val expectedUrl = "https://example.com/tv/戦争と平和.jpg"
        coEvery { tvShowRepository.getTvShowImages(tvId) } returns listOf(expectedUrl)

        // When
        val result = getTvShowImagesUseCase(tvId)

        // Then
        assertThat(result.first()).isEqualTo(expectedUrl)
    }

    @Test
    fun `getTvShowImagesUseCase() should handle long URLs when repository returns them`() = runTest {
        // Given
        val tvId = 4L
        val longUrl = buildString {
            append("https://example.com/very/long/path/to/the/tv/show/images")
            append("/with/many/subdirectories/and/parameters?query=123&param=abc")
        }
        coEvery { tvShowRepository.getTvShowImages(tvId) } returns listOf(longUrl)

        // When
        val result = getTvShowImagesUseCase(tvId)

        // Then
        assertThat(result.first()).hasLength(longUrl.length)
    }

    @Test
    fun `getTvShowImagesUseCase() should make exactly one repository call per invocation`() = runTest {
        // Given
        val tvId = 5L
        coEvery { tvShowRepository.getTvShowImages(tvId) } returns emptyList()

        // When
        getTvShowImagesUseCase(tvId)

        // Then
        coVerify(exactly = 1) { tvShowRepository.getTvShowImages(tvId) }
    }

    @Test
    fun `getTvShowImagesUseCase() should return different URLs for different TV show IDs`() = runTest {
        // Given
        val tvId1 = 6L
        val tvId2 = 7L
        val urls1 = listOf("https://example.com/show1.jpg")
        val urls2 = listOf("https://example.com/show2.jpg")
        coEvery { tvShowRepository.getTvShowImages(tvId1) } returns urls1
        coEvery { tvShowRepository.getTvShowImages(tvId2) } returns urls2

        // When
        val result1 = getTvShowImagesUseCase(tvId1)
        val result2 = getTvShowImagesUseCase(tvId2)

        // Then
        assertThat(result1).isNotEqualTo(result2)
        assertThat(result1.first()).endsWith("show1.jpg")
        assertThat(result2.first()).endsWith("show2.jpg")
    }

    @Test
    fun `getTvShowImagesUseCase() should return secure HTTPS URLs when available`() = runTest {
        // Given
        val tvId = 8L
        val expectedUrl = "https://secure.example.com/image.jpg"
        coEvery { tvShowRepository.getTvShowImages(tvId) } returns listOf(expectedUrl)

        // When
        val result = getTvShowImagesUseCase(tvId)

        // Then
        assertThat(result.first()).startsWith("https://")
    }

    @Test
    fun `getTvShowImagesUseCase() should preserve URL query parameters when present`() = runTest {
        // Given
        val tvId = 9L
        val expectedUrl = "https://example.com/image.jpg?width=500&quality=80"
        coEvery { tvShowRepository.getTvShowImages(tvId) } returns listOf(expectedUrl)

        // When
        val result = getTvShowImagesUseCase(tvId)

        // Then
        assertThat(result.first()).contains("?")
        assertThat(result.first()).contains("width=500")
        assertThat(result.first()).contains("quality=80")
    }

    @Test
    fun `getTvShowImagesUseCase() should handle multiple image URLs when returned`() = runTest {
        // Given
        val tvId = 10L
        val expectedUrls = List(20) { "https://example.com/image${it + 1}.jpg" }
        coEvery { tvShowRepository.getTvShowImages(tvId) } returns expectedUrls

        // When
        val result = getTvShowImagesUseCase(tvId)

        // Then
        assertThat(result).hasSize(20)
        assertThat(result.last()).endsWith("image20.jpg")
    }
}