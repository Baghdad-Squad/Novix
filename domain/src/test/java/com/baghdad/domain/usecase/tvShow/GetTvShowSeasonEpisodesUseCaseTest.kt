package com.baghdad.domain.usecase.tvShow

import com.baghdad.domain.repository.TvShowRepository
import com.baghdad.domain.usecase.episode.EpisodeMock
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

class GetTvShowSeasonEpisodesUseCaseTest {

    private val tvShowRepository = mockk<TvShowRepository>()
    private val getTvShowSeasonEpisodesUseCase = GetTvShowSeasonEpisodesUseCase(tvShowRepository)

    @Test
    fun `getTvShowSeasonEpisodesUseCase should return episodes when called with valid season`() =
        runTest {

            coEvery {
                tvShowRepository.getTvShowSeasonEpisodes(
                    tvId,
                    seasonNumber
                )
            } returns episodes

        val result = getTvShowSeasonEpisodesUseCase(tvId, seasonNumber)

            assertThat(result).containsExactlyElementsIn(episodes)
    }

    @Test
    fun `getTvShowSeasonEpisodesUseCase should return empty list when repository returns no episodes `() =
        runTest {
        coEvery { tvShowRepository.getTvShowSeasonEpisodes(tvId, seasonNumber) } returns emptyList()

        val result = getTvShowSeasonEpisodesUseCase(tvId, seasonNumber)

        assertThat(result).isEmpty()
    }

    private companion object {
        val episodes = EpisodeMock.EPISODES
        val tvId = TvShowMock.TV_SHOW_ID
        val seasonNumber = 5

    }
}