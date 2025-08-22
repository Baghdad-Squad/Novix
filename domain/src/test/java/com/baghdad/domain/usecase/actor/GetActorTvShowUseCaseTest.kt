package com.baghdad.domain.usecase.actor

import com.baghdad.domain.repository.ActorRepository
import com.baghdad.domain.usecase.tvShow.TvShowMock
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

class GetActorTvShowUseCaseTest {

    private val actorRepository = mockk<ActorRepository>()
    private val getActorTvShowUseCase = GetActorTvShowUseCase(actorRepository)

    @Test
    fun `getActorTvShowUseCase should return tv shows when actor has appearances`() = runTest {
        coEvery { actorRepository.getActorTvShows(actorId) } returns listOf(tvShow)

        val result = getActorTvShowUseCase(actorId)

        assertThat(result).isEqualTo(listOf(tvShow))
    }

    private companion object {
        val tvShow = TvShowMock.TV_SHOW
        val actorId = ActorMock.ACTOR_ID
    }
}