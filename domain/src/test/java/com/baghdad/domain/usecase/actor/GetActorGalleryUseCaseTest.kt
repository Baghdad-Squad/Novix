package com.baghdad.domain.usecase.actor

import com.baghdad.domain.repository.ActorRepository
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class GetActorGalleryUseCaseTest {

    private lateinit var actorRepository: ActorRepository
    private lateinit var getActorGalleryUseCase: GetActorGalleryUseCase
    @BeforeEach
    fun setUp() {
        actorRepository = mockk(relaxed = true)
        getActorGalleryUseCase = GetActorGalleryUseCase(actorRepository)
    }

    @Test
    fun `getActorGalleryUseCase should return actor gallery`() = runTest {
        // Given
        val actorId = 1L
        val expectedGallery = listOf("image1.jpg", "image2.jpg")

        coEvery { actorRepository.getActorGallery(actorId) } returns expectedGallery
        // When
        val result = getActorGalleryUseCase(actorId)

        // Then
        assertThat(result).isEqualTo(expectedGallery)
    }

    @Test
    fun `getActorGalleryUseCase should return empty list when no gallery found`() = runTest {
        // Given
        val actorId = 2L
        val expectedGallery = emptyList<String>()

        coEvery { actorRepository.getActorGallery(actorId) } returns expectedGallery
        // When
        val result = getActorGalleryUseCase(actorId)

        // Then
        assertThat(result).isEmpty()
    }


    @Test
    fun `getActorGalleryUseCase should return empty when actorId is non-existing`() = runTest {
        // Given
        val notExistingActorId = 1223312L

        coEvery { actorRepository.getActorGallery(notExistingActorId) } returns emptyList()

        val result = getActorGalleryUseCase(notExistingActorId)
        // When & Then
        assertThat(result).isEqualTo(emptyList<String>())
    }

    @Test
    fun `getActorGalleryUseCase returns single image gallery correctly`() = runTest {
        // Given
        val actorId = 3L
        val singleImageGallery = listOf("single_image.jpg")
        coEvery { actorRepository.getActorGallery(actorId) } returns singleImageGallery

        // When
        val result = getActorGalleryUseCase(actorId)

        // Then
        assertThat(result)
            .hasSize(1)
            .equals("single_image.jpg")
    }

    @Test
    fun `getActorGalleryUseCase returns gallery with different image formats`() = runTest {
        // Given
        val actorId = 6L
        val mixedFormatGallery = listOf(
            "image1.jpg",
            "image2.png",
            "image3.webp",
            "image4.gif"
        )
        coEvery { actorRepository.getActorGallery(actorId) } returns mixedFormatGallery

        // When
        val result = getActorGalleryUseCase(actorId)

        // Then
        assertThat(result).hasSize(4)
        assertThat(result).containsExactlyElementsIn(mixedFormatGallery)
    }

    @Test
    fun `getActorGalleryUseCase calls repository exactly once`() = runTest {
        // Given
        val actorId = 7L
        val sampleGallery = listOf("simple.jpg")

        coEvery { actorRepository.getActorGallery(actorId) } returns sampleGallery

        // When
        getActorGalleryUseCase(actorId)

        // Then
        coVerify(exactly = 1) { actorRepository.getActorGallery(actorId) }
    }

    @Test
    fun `getActorGalleryUseCase with different actor IDs returns different galleries`() = runTest {
        // Given
        val actorId1 = 8L
        val gallery1 = listOf("actor8_1.jpg", "actor8_2.jpg")
        val actorId2 = 9L
        val gallery2 = listOf("actor9_1.jpg")

        coEvery { actorRepository.getActorGallery(actorId1) } returns gallery1
        coEvery { actorRepository.getActorGallery(actorId2) } returns gallery2

        // When
        val result1 = getActorGalleryUseCase(actorId1)
        val result2 = getActorGalleryUseCase(actorId2)

        // Then
        assertThat(result1).isNotEqualTo(result2)
        assertThat(result1).hasSize(2)
        assertThat(result2).hasSize(1)
    }

}