package com.baghdad.domain.usecase.tvShow

import com.baghdad.domain.repository.TvShowRepository
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.just
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

class DeleteTvShowRateUseCaseTest {

    private val tvShowRepository = mockk<TvShowRepository>()
    private val deleteTvShowRateUseCase = DeleteTvShowRateUseCase(tvShowRepository)

    @Test
    fun `deleteTvShowRateUseCase should call tvShowRepository deleteTvShowRate when called`() =
        runTest {
            coEvery { tvShowRepository.deleteTvShowRate(tvShowId) } just Runs

            deleteTvShowRateUseCase(tvShowId)

            coVerify { tvShowRepository.deleteTvShowRate(tvShowId) }
        }

    private companion object {
        val tvShowId = TvShowMock.TV_SHOW_ID
    }
}