package com.baghdad.domain.usecase.tvShow

import com.baghdad.domain.repository.TvShowRepository
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

class GetTvShowDetailsUseCaseTest {

    private val tvShowRepository = mockk<TvShowRepository>()
    private val getTvShowDetailsUseCase = GetTvShowDetailsUseCase(tvShowRepository)

    @Test
    fun `getTvShowDetailsUseCase should return tv show details when repository returns data`() =
        runTest {
            coEvery { tvShowRepository.getTvShowDetails(tvId) } returns tvShow

            val result = getTvShowDetailsUseCase(tvId)

            assertThat(result).isEqualTo(tvShow)
        }

    companion object {
        val tvId = TvShowMock.TV_SHOW_ID
        val tvShow = TvShowMock.TV_SHOW
    }
}