package com.baghdad.domain.usecase.actor

import com.baghdad.domain.repository.ActorRepository
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

class GetActorInfoUseCaseTest {

    private val actorRepository = mockk<ActorRepository>()
    private val getActorInfoUseCase = GetActorInfoUseCase(actorRepository)

    @Test
    fun `getActorInfoUseCase should return actor info when called with valid id`() = runTest {
        coEvery { actorRepository.getActorDetails(actorId) } returns actor

        val result = getActorInfoUseCase(actorId)

        assertThat(result).isEqualTo(actor)
    }

    private companion object {
        val actorId = ActorMock.ACTOR_ID
        val actor = ActorMock.ACTOR
    }
}