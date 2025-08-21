package com.baghdad.domain.usecase.tvShow

import com.baghdad.domain.repository.TvShowRepository
import com.baghdad.entity.media.TvShow
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

class GetPopularTvShowsUseCaseTest {

    private val tvShowRepository = mockk<TvShowRepository>()
    private val getPopularTvShowsUseCase = GetPopularTvShowsUseCase(tvShowRepository)

    @Test
    fun `getPopularTvShowsUseCase() should return popular tv shows when repository returns data`() =
        runTest {
            coEvery { tvShowRepository.getPopularTvShows() } returns tvShows

            val result = getPopularTvShowsUseCase()

            assertThat(result).isEqualTo(tvShows)
        }

    @Test
    fun `getPopularTvShowsUseCase should return empty list when repository returns empty list`() =
        runTest {
            val expected = emptyList<TvShow>()
            coEvery { tvShowRepository.getPopularTvShows() } returns expected

            val result = getPopularTvShowsUseCase()

            assertThat(result).isEmpty()
        }

    companion object {
        val tvShows = TvShowMock.TV_SHOWS
    }
}