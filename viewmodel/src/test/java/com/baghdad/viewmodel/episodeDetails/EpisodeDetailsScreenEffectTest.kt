package com.baghdad.viewmodel.episodeDetails

import com.baghdad.viewmodel.base.BaseUiEffect
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class EpisodeDetailsScreenEffectTest {

    @Test
    fun `NavigateToCategoryTvShows with same ids should be equal`() {
        val effect1 = EpisodeDetailsScreenEffect.NavigateToCategoryTvShows(CATEGORY_ID)
        val effect2 = EpisodeDetailsScreenEffect.NavigateToCategoryTvShows(CATEGORY_ID)

        assertEquals(effect1, effect2)
    }

    @Test
    fun `NavigateToCategoryTvShows copy function should work correctly`() {
        val originalEffect = EpisodeDetailsScreenEffect.NavigateToCategoryTvShows(123L)
        val copiedEffect = originalEffect.copy(categoryId = 456L)

        Assertions.assertEquals(456L, copiedEffect.categoryId)
        Assertions.assertEquals(123L, originalEffect.categoryId)
    }

    @Test
    fun `NavigateToActorDetails should set id correctly`() {
        val effect = EpisodeDetailsScreenEffect.NavigateToActorDetails(ACTOR_ID)

        Assertions.assertEquals(ACTOR_ID, effect.actorId)
    }

    @Test
    fun `NavigateToActorDetails with different ids should not be equal`() {
        val effect1 = EpisodeDetailsScreenEffect.NavigateToActorDetails(123L)
        val effect2 = EpisodeDetailsScreenEffect.NavigateToActorDetails(456L)

        assertNotEquals(effect1, effect2)
    }

    @Test
    fun `NavigateToActorDetails copy function should work correctly`() {
        val originalEffect = EpisodeDetailsScreenEffect.NavigateToActorDetails(123L)
        val copiedEffect = originalEffect.copy(actorId = 456L)

        assertEquals(456L, copiedEffect.actorId)
        assertEquals(123L, originalEffect.actorId)
    }

    @Test
    fun `NavigateBack should be instance of EpisodeDetailsScreenEffect`() {
        val effect = EpisodeDetailsScreenEffect.NavigateBack
        assertTrue(effect is EpisodeDetailsScreenEffect)
    }

    @Test
    fun `NavigateBack should be instance of BaseUiEffect`() {
        val effect = EpisodeDetailsScreenEffect.NavigateBack
        Assertions.assertTrue(effect is BaseUiEffect)
    }

    @Test
    fun `NavigateToLogin should be instance of EpisodeDetailsScreenEffect`() {
        val effect = EpisodeDetailsScreenEffect.NavigateToLogin
       assertTrue(effect is EpisodeDetailsScreenEffect)
    }

    @Test
    fun `NavigateToLogin should be instance of BaseUiEffect`() {
        val effect = EpisodeDetailsScreenEffect.NavigateToLogin
        assertTrue(effect is BaseUiEffect)
    }

    @Test
    fun `all effects should be usable in collections`() {
        val effects = listOf<EpisodeDetailsScreenEffect>(
            EpisodeDetailsScreenEffect.NavigateBack,
            EpisodeDetailsScreenEffect.NavigateToLogin,
            EpisodeDetailsScreenEffect.NavigateToCategoryTvShows(CATEGORY_ID),
            EpisodeDetailsScreenEffect.NavigateToActorDetails(ACTOR_ID)
        )

        assertEquals(4, effects.size)
        assertTrue(effects.all { true })
    }

    @Test
    fun `component functions should work for data classes`() {
        val navigateToCategory = EpisodeDetailsScreenEffect.NavigateToCategoryTvShows(123L)
        val (categoryIdComponent) = navigateToCategory
        Assertions.assertEquals(123L, categoryIdComponent)

        val navigateToActor = EpisodeDetailsScreenEffect.NavigateToActorDetails(456L)
        val (actorIdComponent) = navigateToActor
        Assertions.assertEquals(456L, actorIdComponent)
    }

    @Test
    fun `toString should work correctly for all effects`() {
        val navigateToCategory = EpisodeDetailsScreenEffect.NavigateToCategoryTvShows(123L)
        val navigateBack = EpisodeDetailsScreenEffect.NavigateBack
        val navigateToLogin = EpisodeDetailsScreenEffect.NavigateToLogin
        val navigateToActor = EpisodeDetailsScreenEffect.NavigateToActorDetails(456L)

        assertTrue(navigateToCategory.toString().contains("NavigateToCategoryTvShows"))
        assertTrue(navigateToCategory.toString().contains("123"))
        assertEquals("NavigateBack", navigateBack.toString())
        assertEquals("NavigateToLogin", navigateToLogin.toString())
        assertTrue(navigateToActor.toString().contains("NavigateToActorDetails"))
        assertTrue(navigateToActor.toString().contains("456"))
    }

    private companion object {
        const val CATEGORY_ID = 123L
        const val ACTOR_ID = 456L
    }
}