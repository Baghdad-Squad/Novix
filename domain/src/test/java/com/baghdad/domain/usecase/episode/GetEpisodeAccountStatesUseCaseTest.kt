package com.baghdad.domain.usecase.episode

import com.baghdad.domain.repository.EpisodeRepository
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class GetEpisodeAccountStatesUseCaseTest {

    private lateinit var episodeRepository: EpisodeRepository
    private lateinit var getEpisodeAccountStatesUseCase: GetEpisodeAccountStatesUseCase

    @BeforeEach
    fun setUp() {
        episodeRepository = mockk(relaxed = true)
        getEpisodeAccountStatesUseCase = GetEpisodeAccountStatesUseCase(episodeRepository)
    }

    @Test
    fun `should return true if user has rated an episode with valid parameters`() = runTest {
        coEvery {
            episodeRepository.getEpisodeAccountStates(TV_SHOW_ID, SEASON_NUMBER, EPISODE_NUMBER)
        } returns true

        val result = getEpisodeAccountStatesUseCase(TV_SHOW_ID, SEASON_NUMBER, EPISODE_NUMBER)

        assertThat(result).isTrue()
    }

    companion object {
        private const val TV_SHOW_ID = 123L
        private const val SEASON_NUMBER = 1
        private const val EPISODE_NUMBER = 2
    }
}
