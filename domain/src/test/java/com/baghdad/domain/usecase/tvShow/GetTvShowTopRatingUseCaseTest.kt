package com.baghdad.domain.usecase.tvShow

import com.baghdad.domain.repository.TvShowRepository
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

class GetTvShowTopRatingUseCaseTest {

    private val tvShowRepository = mockk<TvShowRepository>()
    private val getTvShowTopRatingUseCase = GetTvShowTopRatingUseCase(tvShowRepository)

    @Test
    fun `getTopRatedTvShows should return all tv shows when genreId is null`() = runTest {
        coEvery { tvShowRepository.getTopRatedTvShows(page) } returns tvShowPagedResult

        val result = getTvShowTopRatingUseCase(page, genreId = null)

        assertThat(result.data).containsExactlyElementsIn(tvShows)
    }

    private companion object {
        val tvShows = TvShowMock.TV_SHOWS
        val page = 1
        val tvShowPagedResult = TvShowMock.TV_SHOW_RESULT
    }
}