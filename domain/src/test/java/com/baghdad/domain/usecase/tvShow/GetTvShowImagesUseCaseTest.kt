package com.baghdad.domain.usecase.tvShow

import com.baghdad.domain.repository.TvShowRepository
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

class GetTvShowImagesUseCaseTest {

    private val tvShowRepository = mockk<TvShowRepository>()
    private val getTvShowImagesUseCase = GetTvShowImagesUseCase(tvShowRepository)

    @Test
    fun `getTvShowImagesUseCase should return image URLs when repository returns data`() =
        runTest {
            coEvery { tvShowRepository.getTvShowImages(tvId) } returns imagesURL

            val result = getTvShowImagesUseCase(tvId)

            assertThat(result).isEqualTo(imagesURL)
        }

    @Test
    fun `getTvShowImagesUseCase should return empty list when repository returns no images`() =
        runTest {
            coEvery { tvShowRepository.getTvShowImages(tvId) } returns emptyList()

            val result = getTvShowImagesUseCase(tvId)

            assertThat(result).isEmpty()
        }

    private companion object {
        val tvId = TvShowMock.TV_SHOW_ID
        val imagesURL = TvShowMock.TV_SHOW.headerImagesURLs
    }
}