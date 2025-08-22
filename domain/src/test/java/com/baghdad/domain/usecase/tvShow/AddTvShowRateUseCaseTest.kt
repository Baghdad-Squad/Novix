package com.baghdad.domain.usecase.tvShow

import com.baghdad.domain.repository.TvShowRepository
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.just
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

class AddTvShowRateUseCaseTest {

    private val tvShowRepository = mockk<TvShowRepository>()
    private val addTvShowRateUseCase = AddTvShowRateUseCase(tvShowRepository)

    @Test
    fun `addTvShowRateUseCase should call tvShowRepository addTvShowRate when called`() = runTest {
        coEvery { tvShowRepository.addTvShowRate(tvShowId, rating) } just Runs

        addTvShowRateUseCase(tvShowId, rating)

        coVerify { tvShowRepository.addTvShowRate(tvShowId, rating) }
    }

    private companion object {
        val tvShowId = TvShowMock.TV_SHOW_ID
        val rating = 22
    }
}