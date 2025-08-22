package com.baghdad.domain.usecase.episode

import com.baghdad.domain.repository.EpisodeRepository
import com.baghdad.domain.usecase.tvShow.TvShowMock
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

class GetEpisodeAccountStatesUseCaseTest {

    private val episodeRepository = mockk<EpisodeRepository>()
    private val getEpisodeAccountStatesUseCase = GetEpisodeAccountStatesUseCase(episodeRepository)

    @Test
    fun `getEpisodeAccountStatesUseCase should return true  when user has rated an episode`() =
        runTest {
        coEvery {
            episodeRepository.getEpisodeAccountStates(tvShowId, seasonNumber, episodeNumber)
        } returns true

            val result = getEpisodeAccountStatesUseCase(tvShowId, seasonNumber, episodeNumber)

        assertThat(result).isTrue()
    }

    private companion object {
        val tvShowId = TvShowMock.TV_SHOW_ID
        val seasonNumber = TvShowMock.TV_SHOW.numberOfSeasons
        val episodeNumber = EpisodeMock.EPISODE.episodeNumber
    }
}
