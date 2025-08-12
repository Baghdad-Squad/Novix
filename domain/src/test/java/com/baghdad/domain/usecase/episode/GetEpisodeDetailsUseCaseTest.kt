package com.baghdad.domain.usecase.episode

import com.baghdad.domain.repository.EpisodeRepository
import com.baghdad.domain.testHelper.getSampleEpisode
import com.baghdad.entity.media.Episode
import com.baghdad.entity.media.Genre
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.LocalDate
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class GetEpisodeDetailsUseCaseTest {

    private lateinit var episodeRepository: EpisodeRepository
    private lateinit var getEpisodeDetailsUseCase: GetEpisodeDetailsUseCase

    @BeforeEach
    fun setUp() {
        episodeRepository = mockk()
        getEpisodeDetailsUseCase = GetEpisodeDetailsUseCase(episodeRepository)
    }

    @Test
    fun `should return correct episode when called with valid tvShowId seasonNumber and episodeNumber`() = runTest {
        coEvery {
            episodeRepository.getEpisodeDetails(TV_SHOW_ID_1, SEASON_NUMBER_1, EPISODE_NUMBER_1)
        } returns sampleEpisode

        val result = getEpisodeDetailsUseCase(TV_SHOW_ID_1, SEASON_NUMBER_1, EPISODE_NUMBER_1)

        assertThat(result).isEqualTo(sampleEpisode)
    }

    @Test
    fun `invoke() should return correct episode data when called with different tvShowId, seasonNumber and episodeNumber`() = runTest {
        coEvery {
            episodeRepository.getEpisodeDetails(TV_SHOW_ID_2, SEASON_NUMBER_2, EPISODE_NUMBER_2)
        } returns differentEpisode

        val result = getEpisodeDetailsUseCase(TV_SHOW_ID_2, SEASON_NUMBER_2, EPISODE_NUMBER_2)

        assertThat(result).isEqualTo(differentEpisode)
        assertThat(result.id).isEqualTo(TV_SHOW_ID_2)
        assertThat(result.title).isEqualTo(TV_SHOW_TITLE)
    }

    companion object {
        private val sampleEpisode = getSampleEpisode()
        private val differentEpisode = getSampleEpisode(
            id =  TV_SHOW_ID_2,
            title = TV_SHOW_TITLE,
            currentSeason = SEASON_NUMBER_2,
            episodeNumber = EPISODE_NUMBER_2
        )

        private const val TV_SHOW_ID_1 = 123L
        private const val TV_SHOW_ID_2 = 456L

        private const val SEASON_NUMBER_1 = 2
        private const val SEASON_NUMBER_2 = 3

        private const val EPISODE_NUMBER_1 = 1
        private const val EPISODE_NUMBER_2 = 2

        private const val TV_SHOW_TITLE = "The Long Night"
    }
}
