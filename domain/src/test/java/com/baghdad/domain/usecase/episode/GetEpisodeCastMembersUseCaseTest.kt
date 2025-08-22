package com.baghdad.domain.usecase.episode

import com.baghdad.domain.repository.EpisodeRepository
import com.baghdad.domain.usecase.actor.ActorMock
import com.baghdad.domain.usecase.tvShow.TvShowMock
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

class GetEpisodeCastMembersUseCaseTest {

    private val episodeRepository = mockk<EpisodeRepository>()
    private val getEpisodeCastMembersUseCase = GetEpisodeCastMembersUseCase(episodeRepository)

    @Test
    fun `getEpisodeCastMembersUseCase should return cast members when executing`() = runTest {
        coEvery {
            episodeRepository.getEpisodeCastMembers(tvShowId, seasonNumber, episodeNumber)
        } returns expectedCastMembers

        val result = getEpisodeCastMembersUseCase(tvShowId, seasonNumber, episodeNumber)

        assertThat(result).isEqualTo(expectedCastMembers)
    }

    private companion object {
        val expectedCastMembers = ActorMock.CAST_MEMBERS
        val tvShowId = TvShowMock.TV_SHOW_ID
        val seasonNumber = TvShowMock.TV_SHOW.numberOfSeasons
        val episodeNumber = EpisodeMock.EPISODE.episodeNumber
    }
}