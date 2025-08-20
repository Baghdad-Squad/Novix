package com.baghdad.domain.usecase.tvShow

import com.baghdad.domain.repository.TvShowRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class GetTvShowAccountStatesUseCaseTest {

    private val tvShowRepository = mockk<TvShowRepository>()
    private val getTvShowAccountStatesUseCase = GetTvShowAccountStatesUseCase(tvShowRepository)

    @Test
    fun `getTvShowAccountStatesUseCase should return account states when repository returns data`() =
        runTest {
            coEvery { tvShowRepository.getTvShowAccountStates(tvShowId) } returns true

            val result = getTvShowAccountStatesUseCase(tvShowId)

            assertEquals(true, result)
        }

    companion object {
        val tvShowId = TvShowMock.TV_SHOW_ID
    }
}