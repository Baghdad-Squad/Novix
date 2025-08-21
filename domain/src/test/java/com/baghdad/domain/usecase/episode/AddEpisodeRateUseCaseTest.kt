package com.baghdad.domain.usecase.episode

import com.baghdad.domain.repository.EpisodeRepository
import com.baghdad.domain.usecase.tvShow.TvShowMock
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

class AddEpisodeRateUseCaseTest {

    private val episodeRepository = mockk<EpisodeRepository>()
    private val addEpisodeRateUseCase = AddEpisodeRateUseCase(episodeRepository)


    @Test
    fun `addEpisodeRateUseCase should call repository when called with correct parameters`() =
        runTest {
            coEvery {
                episodeRepository.addTvEpisodeRate(tvShowId, seasonNumber, episodeNumber, ratting)
            } returns Unit

            addEpisodeRateUseCase(tvShowId, seasonNumber, episodeNumber, ratting)

            coVerify(exactly = 1) {
                episodeRepository.addTvEpisodeRate(tvShowId, seasonNumber, episodeNumber, ratting)
            }
        }

    private companion object {
        val tvShowId = TvShowMock.TV_SHOW_ID
        val seasonNumber = TvShowMock.TV_SHOW.numberOfSeasons
        val episodeNumber = EpisodeMock.EPISODE.episodeNumber
        val ratting = 8
    }
}