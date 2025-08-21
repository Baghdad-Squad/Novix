package com.baghdad.domain.usecase.episode

import com.baghdad.domain.repository.EpisodeRepository
import com.baghdad.domain.usecase.tvShow.TvShowMock
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

class GetEpisodeDetailsUseCaseTest {

    private val episodeRepository = mockk<EpisodeRepository>()
    private val getEpisodeDetailsUseCase = GetEpisodeDetailsUseCase(episodeRepository)


    @Test
    fun `getEpisodeDetailsUseCase should return correct episode when called `() = runTest {
        coEvery {
            episodeRepository.getEpisodeDetails(tvShowId, seasonNumber, episodeNumber)
        } returns episode

        val result = getEpisodeDetailsUseCase(tvShowId, seasonNumber, episodeNumber)

        assertThat(result).isEqualTo(episode)
    }

   private companion object {
        val episode = EpisodeMock.EPISODE
        val tvShowId = TvShowMock.TV_SHOW_ID
        val seasonNumber = TvShowMock.TV_SHOW.numberOfSeasons
        val episodeNumber = EpisodeMock.EPISODE.episodeNumber
    }
}
