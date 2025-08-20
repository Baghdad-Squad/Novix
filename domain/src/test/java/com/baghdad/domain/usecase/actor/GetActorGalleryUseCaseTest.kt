package com.baghdad.domain.usecase.actor

import com.baghdad.domain.repository.ActorRepository
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

class GetActorGalleryUseCaseTest {

    private val actorRepository = mockk<ActorRepository>()
    private val getActorGalleryUseCase = GetActorGalleryUseCase(actorRepository)

    @Test
    fun `getActorGalleryUseCase should return actor gallery when called with valid actorId`() =
        runTest {
            coEvery { actorRepository.getActorGallery(actorId) } returns images

            val result = getActorGalleryUseCase(actorId)

            assertThat(result).isEqualTo(images)
        }

    private companion object {
        val actorId = ActorMock.ACTOR_ID
        val images = listOf("image1.jpg", "image2.jpg")
    }
}