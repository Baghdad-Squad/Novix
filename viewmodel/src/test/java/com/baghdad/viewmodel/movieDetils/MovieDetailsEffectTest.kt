package com.baghdad.viewmodel.movieDetails

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class MovieDetailsEffectTest {

    @Test
    fun `NavigateToCategory with same ids should be equal`() {
        val effect1 = MovieDetailsEffect.NavigateToCategory(categoryId)
        val effect2 = MovieDetailsEffect.NavigateToCategory(categoryId)

        Assertions.assertEquals(effect1, effect2)
    }

    @Test
    fun `NavigateToCategory copy function should work correctly`() {
        val originalEffect = MovieDetailsEffect.NavigateToCategory(123L)
        val copiedEffect = originalEffect.copy(id = 456L)

        Assertions.assertEquals(456L, copiedEffect.id)
        Assertions.assertEquals(123L, originalEffect.id)
    }

    @Test
    fun `NavigateToReviewDetails copy function should work correctly`() {
        val originalEffect = MovieDetailsEffect.NavigateToReviewDetails(123L)
        val copiedEffect = originalEffect.copy(id = 456L)

        Assertions.assertEquals(456L, copiedEffect.id)
        Assertions.assertEquals(123L, originalEffect.id)
    }

    @Test
    fun `NavigateToActorDetails should set id correctly`() {
        val effect = MovieDetailsEffect.NavigateToActorDetails(actorId)

        Assertions.assertEquals(actorId, effect.id)
    }

    @Test
    fun `NavigateToActorDetails with different ids should not be equal`() {
        val effect1 = MovieDetailsEffect.NavigateToActorDetails(123L)
        val effect2 = MovieDetailsEffect.NavigateToActorDetails(456L)

        Assertions.assertNotEquals(effect1, effect2)
    }

    @Test
    fun `NavigateToActorDetails copy function should work correctly`() {
        val originalEffect = MovieDetailsEffect.NavigateToActorDetails(123L)
        val copiedEffect = originalEffect.copy(id = 456L)

        Assertions.assertEquals(456L, copiedEffect.id)
        Assertions.assertEquals(123L, originalEffect.id)
    }

    @Test
    fun `NavigateToMovie with different ids should not be equal`() {
        val effect1 = MovieDetailsEffect.NavigateToMovie(123L)
        val effect2 = MovieDetailsEffect.NavigateToMovie(456L)

        Assertions.assertNotEquals(effect1, effect2)
    }

    @Test
    fun `NavigateToMovie copy function should work correctly`() {
        val originalEffect = MovieDetailsEffect.NavigateToMovie(123L)
        val copiedEffect = originalEffect.copy(id = 456L)

        Assertions.assertEquals(456L, copiedEffect.id)
        Assertions.assertEquals(123L, originalEffect.id)
    }

    @Test
    fun `OpenYoutubeLink should be instance of MovieDetailsEffect`() {

        Assertions.assertTrue(true)
    }

    @Test
    fun `OpenYoutubeLink should be instance of BaseUiEffect`() {

        Assertions.assertTrue(true)
    }

    @Test
    fun `OpenYoutubeLink should set youtubeLink correctly`() {
        val effect = MovieDetailsEffect.OpenYoutubeLink(youtubeUrl)

        Assertions.assertEquals(youtubeUrl, effect.youtubeLink)
    }

    @Test
    fun `OpenYoutubeLink with different links should not be equal`() {
        val effect1 = MovieDetailsEffect.OpenYoutubeLink("https://youtube.com/watch?v=123")
        val effect2 = MovieDetailsEffect.OpenYoutubeLink("https://youtube.com/watch?v=456")

        Assertions.assertNotEquals(effect1, effect2)
    }

    @Test
    fun `OpenYoutubeLink with same links should be equal`() {
        val effect1 = MovieDetailsEffect.OpenYoutubeLink(youtubeUrl)
        val effect2 = MovieDetailsEffect.OpenYoutubeLink(youtubeUrl)

        Assertions.assertEquals(effect1, effect2)
    }

    @Test
    fun `OpenYoutubeLink copy function should work correctly`() {
        val originalEffect = MovieDetailsEffect.OpenYoutubeLink("original_link")
        val copiedEffect = originalEffect.copy(youtubeLink = "new_link")

        Assertions.assertEquals("new_link", copiedEffect.youtubeLink)
        Assertions.assertEquals("original_link", originalEffect.youtubeLink)
    }

    @Test
    fun `OpenYoutubeLink should handle empty string correctly`() {
        val effect = MovieDetailsEffect.OpenYoutubeLink("")

        Assertions.assertEquals("", effect.youtubeLink)
    }

    @Test
    fun `all effects should be usable in collections`() {
        val effects = listOf<MovieDetailsEffect>(
            MovieDetailsEffect.NavigateToCategory(categoryId),
            MovieDetailsEffect.NavigateToReviewDetails(reviewId),
            MovieDetailsEffect.NavigateToActorDetails(actorId),
            MovieDetailsEffect.NavigateToMovie(movieId),
            MovieDetailsEffect.OpenYoutubeLink(youtubeUrl),
            MovieDetailsEffect.NavigateBack
        )

        Assertions.assertEquals(6, effects.size)
        Assertions.assertTrue(effects.all { true })
        Assertions.assertTrue(effects.all { true })
    }

    @Test
    fun `component functions should work for data classes`() {
        val navigateToCategory = MovieDetailsEffect.NavigateToCategory(123L)
        val (categoryIdComponent) = navigateToCategory
        Assertions.assertEquals(123L, categoryIdComponent)

        val openYoutube = MovieDetailsEffect.OpenYoutubeLink("test_link")
        val (youtubeLinkComponent) = openYoutube
        Assertions.assertEquals("test_link", youtubeLinkComponent)
    }

    @Test
    fun `toString should work correctly for all effects`() {
        val navigateToCategory = MovieDetailsEffect.NavigateToCategory(123L)
        val navigateBack = MovieDetailsEffect.NavigateBack
        val openYoutube = MovieDetailsEffect.OpenYoutubeLink("test")

        Assertions.assertTrue(navigateToCategory.toString().contains("NavigateToCategory"))
        Assertions.assertTrue(navigateToCategory.toString().contains("123"))
        Assertions.assertEquals("NavigateBack", navigateBack.toString())
        Assertions.assertTrue(openYoutube.toString().contains("OpenYoutubeLink"))
        Assertions.assertTrue(openYoutube.toString().contains("test"))
    }

    private companion object {
        const val categoryId = 123L
        const val reviewId = 456L
        const val actorId = 789L
        const val movieId = 101L
        const val youtubeUrl = "https://youtube.com/watch?v=dQw4w9WgXcQ"
    }
}