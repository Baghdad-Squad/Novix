package com.baghdad.domain.usecase.actor

import com.baghdad.domain.repository.ActorRepository
import com.baghdad.domain.usecase.actor.GetActorGalleryUseCaseTest.Companion.EMPTY_GALLERY
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
    fun `getActorGalleryUseCase() should return actor gallery when called with valid actorId`() = runTest {
        coEvery { actorRepository.getActorGallery(ACTOR_ID_1) } returns GALLERY_2_IMAGES

        val result = getActorGalleryUseCase(ACTOR_ID_1)

        assertThat(result).isEqualTo(GALLERY_2_IMAGES)
    }

    @Test
    fun `getActorGalleryUseCase() should return empty list when no gallery found for actor`() = runTest {
        coEvery { actorRepository.getActorGallery(ACTOR_ID_2) } returns EMPTY_GALLERY

        val result = getActorGalleryUseCase(ACTOR_ID_2)

        assertThat(result).isEmpty()
    }


    @Test
    fun `getActorGalleryUseCase() should return empty list when actorId is non-existing`() = runTest {
        coEvery { actorRepository.getActorGallery(NON_EXISTING_ACTOR_ID) } returns EMPTY_GALLERY

        val result = getActorGalleryUseCase(NON_EXISTING_ACTOR_ID)

        assertThat(result).isEmpty()
    }

    @Test
    fun `getActorGalleryUseCase() should returns one image if actor has single-image gallery`() = runTest {
        coEvery { actorRepository.getActorGallery(ACTOR_ID_3) } returns GALLERY_SINGLE_IMAGE

        val result = getActorGalleryUseCase(ACTOR_ID_3)

        assertThat(result).isEqualTo(GALLERY_SINGLE_IMAGE)
    }

    @Test
    fun `getActorGalleryUseCase() should return gallery with different image formats`() = runTest {
        coEvery { actorRepository.getActorGallery(ACTOR_ID_6) } returns GALLERY_MIXED_FORMATS

        val result = getActorGalleryUseCase(ACTOR_ID_6)

        assertThat(result).containsExactlyElementsIn(GALLERY_MIXED_FORMATS)
    }

    @Test
    fun `getActorGalleryUseCase() should call repository exactly once when invoked`() = runTest {
        coEvery { actorRepository.getActorGallery(ACTOR_ID_7) } returns GALLERY_SAMPLE

        getActorGalleryUseCase(ACTOR_ID_7)

        coVerify(exactly = 1) { actorRepository.getActorGallery(ACTOR_ID_7) }
    }

    @Test
    fun `getActorGalleryUseCase() should return different galleries when called with different actor IDs`() = runTest {
        coEvery { actorRepository.getActorGallery(ACTOR_ID_8) } returns GALLERY_ACTOR_8
        coEvery { actorRepository.getActorGallery(ACTOR_ID_9) } returns GALLERY_ACTOR_9

        val result1 = getActorGalleryUseCase(ACTOR_ID_8)
        val result2 = getActorGalleryUseCase(ACTOR_ID_9)

        assertThat(result1).isNotEqualTo(result2)
    }

    companion object {
        const val ACTOR_ID_1 = 1L
        const val ACTOR_ID_2 = 2L
        const val ACTOR_ID_3 = 3L
        const val ACTOR_ID_6 = 6L
        const val ACTOR_ID_7 = 7L
        const val ACTOR_ID_8 = 8L
        const val ACTOR_ID_9 = 9L
        const val NON_EXISTING_ACTOR_ID = 1223312L

        val EMPTY_GALLERY = emptyList<String>()
        val GALLERY_2_IMAGES = listOf("image1.jpg", "image2.jpg")
        val GALLERY_SINGLE_IMAGE = listOf("single_image.jpg")
        val GALLERY_MIXED_FORMATS = listOf("image1.jpg", "image2.png", "image3.webp", "image4.gif")
        val GALLERY_SAMPLE = listOf("simple.jpg")
        val GALLERY_ACTOR_8 = listOf("actor8_1.jpg", "actor8_2.jpg")
        val GALLERY_ACTOR_9 = listOf("actor9_1.jpg")
    }
}
