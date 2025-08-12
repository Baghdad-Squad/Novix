package com.baghdad.domain.usecase.episode

import com.baghdad.domain.repository.EpisodeRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class AddEpisodeRateUseCaseTest {
    private lateinit var episodeRepository: EpisodeRepository
    private lateinit var addEpisodeRateUseCase: AddEpisodeRateUseCase

    @BeforeEach
    fun setUp() {
        episodeRepository = mockk(relaxed = true)
        addEpisodeRateUseCase = AddEpisodeRateUseCase(episodeRepository)
    }

    @Test
    fun `should call repository with correct parameters`() = runTest {
        coEvery {
            episodeRepository.addTvEpisodeRate(TV_SHOW_ID, SEASON_NUMBER, EPISODE_NUMBER, RATING)
        } returns Unit

        addEpisodeRateUseCase(TV_SHOW_ID, SEASON_NUMBER, EPISODE_NUMBER, RATING)

        coVerify(exactly = 1) {
            episodeRepository.addTvEpisodeRate(TV_SHOW_ID, SEASON_NUMBER, EPISODE_NUMBER, RATING)
        }
    }

    companion object {
        private const val TV_SHOW_ID = 1L
        private const val SEASON_NUMBER = 2
        private const val EPISODE_NUMBER = 3
        private const val RATING = 8
    }
}