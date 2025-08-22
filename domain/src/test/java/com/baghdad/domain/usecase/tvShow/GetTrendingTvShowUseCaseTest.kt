package com.baghdad.domain.usecase.tvShow

import com.baghdad.domain.repository.TvShowRepository
import com.baghdad.domain.usecase.genre.GenreMock
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

class GetTrendingTvShowUseCaseTest {

    private val tvShowRepository = mockk<TvShowRepository>()
    private val getTrendingTvShowUseCase = GetTrendingTvShowUseCase(tvShowRepository)

    @Test
    fun `getTrendingTvShows should return all tv shows with pagination when genreId is null`() =
        runTest {
            coEvery { tvShowRepository.getTrendingTvShows(page) } returns tvShowResult

            val result = getTrendingTvShowUseCase(page, genreId = null)

            assertThat(result).isEqualTo(tvShowResult)
        }

    companion object {
        val tvShow = TvShowMock.TV_SHOW
        val tvShowResult = TvShowMock.TV_SHOW_RESULT
        val genreId = GenreMock.GENRE_ID
        val page = 2
    }
}