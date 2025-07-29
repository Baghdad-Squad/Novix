package com.baghdad.viewmodel.actorGallery

import com.baghdad.viewmodel.base.BaseUiState
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class ActorGalleryScreenStateTest {

    @Test
    fun `default constructor should create state with empty images and not loading`() {
        val state = ActorGalleryScreenState()

        Assertions.assertEquals(emptyList<String>(), state.images)
        Assertions.assertFalse(state.isLoading)
    }

    @Test
    fun `constructor with parameters should set values correctly`() {
        val images = listOf(imageUrl1, imageUrl2, imageUrl3)
        val isLoading = true

        val state = ActorGalleryScreenState(
            images = images,
            isLoading = isLoading
        )

        Assertions.assertEquals(images, state.images)
        Assertions.assertTrue(state.isLoading)
    }

    @Test
    fun `isLoading property should be accessible from BaseUiState interface`() {
        val state: BaseUiState = ActorGalleryScreenState(isLoading = true)

        Assertions.assertTrue(state.isLoading)
    }

    @Test
    fun `state with empty images list should work correctly`() {
        val state = ActorGalleryScreenState(
            images = emptyList(),
            isLoading = false
        )

        Assertions.assertTrue(state.images.isEmpty())
        Assertions.assertFalse(state.isLoading)
    }

    @Test
    fun `state with single image should work correctly`() {
        val singleImage = listOf(imageUrl1)
        val state = ActorGalleryScreenState(
            images = singleImage,
            isLoading = true
        )

        Assertions.assertEquals(1, state.images.size)
        Assertions.assertEquals(imageUrl1, state.images[0])
        Assertions.assertTrue(state.isLoading)
    }

    @Test
    fun `state with multiple images should preserve order`() {
        val images = listOf(imageUrl1, imageUrl2, imageUrl3)
        val state = ActorGalleryScreenState(images = images)

        Assertions.assertEquals(3, state.images.size)
        Assertions.assertEquals(imageUrl1, state.images[0])
        Assertions.assertEquals(imageUrl2, state.images[1])
        Assertions.assertEquals(imageUrl3, state.images[2])
    }

    @Test
    fun `copy function should work correctly with images change`() {
        val originalImages = listOf(imageUrl1)
        val newImages = listOf(imageUrl2, imageUrl3)
        val originalState = ActorGalleryScreenState(images = originalImages, isLoading = false)

        val newState = originalState.copy(images = newImages)

        Assertions.assertEquals(newImages, newState.images)
        Assertions.assertFalse(newState.isLoading)
        Assertions.assertEquals(originalImages, originalState.images)
    }

    @Test
    fun `copy function should work correctly with isLoading change`() {
        val images = listOf(imageUrl1, imageUrl2)
        val originalState = ActorGalleryScreenState(images = images, isLoading = false)

        val newState = originalState.copy(isLoading = true)

        Assertions.assertEquals(images, newState.images)
        Assertions.assertTrue(newState.isLoading)
        Assertions.assertFalse(originalState.isLoading)
    }

    @Test
    fun `copy function should work correctly with both parameters changed`() {
        val originalImages = listOf(imageUrl1)
        val newImages = listOf(imageUrl2, imageUrl3)
        val originalState = ActorGalleryScreenState(images = originalImages, isLoading = false)

        val newState = originalState.copy(images = newImages, isLoading = true)

        Assertions.assertEquals(newImages, newState.images)
        Assertions.assertTrue(newState.isLoading)
        Assertions.assertEquals(originalImages, originalState.images)
        Assertions.assertFalse(originalState.isLoading)
    }

    @Test
    fun `copy function with no parameters should create identical state`() {
        val images = listOf(imageUrl1, imageUrl2)
        val originalState = ActorGalleryScreenState(images = images, isLoading = true)

        val copiedState = originalState.copy()

        Assertions.assertEquals(originalState.images, copiedState.images)
        Assertions.assertEquals(originalState.isLoading, copiedState.isLoading)
    }

    @Test
    fun `equals should work correctly for identical states`() {
        val images = listOf(imageUrl1, imageUrl2)
        val state1 = ActorGalleryScreenState(images = images, isLoading = true)
        val state2 = ActorGalleryScreenState(images = images, isLoading = true)

        Assertions.assertEquals(state1, state2)
    }

    @Test
    fun `equals should work correctly for different states`() {
        val state1 = ActorGalleryScreenState(images = listOf(imageUrl1), isLoading = true)
        val state2 = ActorGalleryScreenState(images = listOf(imageUrl2), isLoading = true)

        Assertions.assertNotEquals(state1, state2)
    }

    @Test
    fun `hashCode should be consistent for identical states`() {
        val images = listOf(imageUrl1, imageUrl2)
        val state1 = ActorGalleryScreenState(images = images, isLoading = true)
        val state2 = ActorGalleryScreenState(images = images, isLoading = true)

        Assertions.assertEquals(state1.hashCode(), state2.hashCode())
    }

    @Test
    fun `toString should contain all properties`() {
        val images = listOf(imageUrl1, imageUrl2)
        val state = ActorGalleryScreenState(images = images, isLoading = true)

        val toString = state.toString()

        Assertions.assertTrue(toString.contains("images"))
        Assertions.assertTrue(toString.contains("isLoading"))
        Assertions.assertTrue(toString.contains("true"))
    }

    @Test
    fun `component functions should work correctly`() {
        val images = listOf(imageUrl1, imageUrl2)
        val state = ActorGalleryScreenState(images = images, isLoading = true)

        val (componentImages, componentIsLoading) = state

        Assertions.assertEquals(images, componentImages)
        Assertions.assertTrue(componentIsLoading)
    }

    @Test
    fun `state should handle large image lists correctly`() {
        val largeImageList = (1..100).map { "image_$it.jpg" }
        val state = ActorGalleryScreenState(images = largeImageList, isLoading = false)

        Assertions.assertEquals(100, state.images.size)
        Assertions.assertEquals("image_1.jpg", state.images[0])
        Assertions.assertEquals("image_100.jpg", state.images[99])
        Assertions.assertFalse(state.isLoading)
    }

    @Test
    fun `state should handle images with special characters`() {
        val specialImages = listOf(
            "image with spaces.jpg",
            "image_with_underscores.png",
            "image-with-dashes.gif",
            "image@with@symbols.jpeg"
        )
        val state = ActorGalleryScreenState(images = specialImages)

        Assertions.assertEquals(4, state.images.size)
        Assertions.assertEquals("image with spaces.jpg", state.images[0])
        Assertions.assertEquals("image@with@symbols.jpeg", state.images[3])
    }

    private companion object {
        const val imageUrl1 = "https://example.com/image1.jpg"
        const val imageUrl2 = "https://example.com/image2.png"
        const val imageUrl3 = "https://example.com/image3.gif"
    }
}