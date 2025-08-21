package com.baghdad.domain.usecase.tvShow

import com.baghdad.domain.model.pagination.PagedResult
import com.baghdad.domain.repository.TvShowRepository
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coVerify
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

    @Test
    fun `getTopRatedTvShows should return empty list when no tv shows match genreId`() = runTest {

        coEvery { tvShowRepository.getTopRatedTvShows(page) } returns tvShowPagedResult

        val result = getTvShowTopRatingUseCase(page, genreId)

        assertThat(result.data).isEmpty()
        coVerify(exactly = 1) { tvShowRepository.getTopRatedTvShows(page) }
    }

    private companion object {
        val tvShows = TvShowMock.TV_SHOWS
        val page = 1
        val genreId = 2L
        val tvShowPagedResult = PagedResult(data = tvShows, nextPage = 2, prevPage = null)
    }
}