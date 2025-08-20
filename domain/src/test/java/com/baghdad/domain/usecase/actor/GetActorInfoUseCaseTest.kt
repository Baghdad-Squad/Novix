package com.baghdad.domain.usecase.actor

import com.baghdad.domain.repository.ActorRepository
import com.baghdad.domain.testHelper.getSampleActor
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class GetActorInfoUseCaseTest {

    private lateinit var actorRepository: ActorRepository
    private lateinit var getActorInfoUseCase: GetActorInfoUseCase
    val sampleActor = getSampleActor()

    @BeforeEach
    fun setUp() {
        actorRepository = mockk(relaxed = true)
        getActorInfoUseCase = GetActorInfoUseCase(actorRepository)
    }

    @Test
    fun `getActorInfoUseCase should return actor info when called with valid id`() = runTest {
        coEvery { actorRepository.getActorDetails(actorId) } returns sampleActor

        val result = getActorInfoUseCase(actorId)

        assertThat(result).isEqualTo(sampleActor)
    }

    private companion object {
        const val actorId = 1L
    }
}