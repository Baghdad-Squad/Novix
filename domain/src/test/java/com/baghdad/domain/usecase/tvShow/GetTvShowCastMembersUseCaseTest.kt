package com.baghdad.domain.usecase.tvShow

import com.baghdad.domain.repository.TvShowRepository
import com.baghdad.domain.usecase.actor.ActorMock
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

class GetTvShowCastMembersUseCaseTest {

    private val tvShowRepository = mockk<TvShowRepository>()
    private val getTvShowCastMembersUseCase = GetTvShowCastMembersUseCase(tvShowRepository)

    @Test
    fun `getTvShowCastMembersUseCase should return cast members when repository returns data`() =
        runTest {
            coEvery { tvShowRepository.getTvShowCastMembers(tvShowId) } returns castMembers

            val result = getTvShowCastMembersUseCase(tvShowId)

            assertThat(result).isEqualTo(castMembers)
        }

    @Test
    fun `getTvShowCastMembersUseCase should return empty list when repository returns no data`() =
        runTest {
            coEvery { tvShowRepository.getTvShowCastMembers(tvShowId) } returns emptyList()

            val result = getTvShowCastMembersUseCase(tvShowId)

            assertThat(result).isEmpty()
        }

    private companion object {
        val tvShowId = TvShowMock.TV_SHOW_ID
        val castMembers = ActorMock.CAST_MEMBERS
    }
}