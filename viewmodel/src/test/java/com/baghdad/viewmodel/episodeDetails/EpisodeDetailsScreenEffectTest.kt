package com.baghdad.viewmodel.episodeDetails

import com.baghdad.viewmodel.base.BaseUiEffect
import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.Test

class EpisodeDetailsScreenEffectTest {

    @Test
    fun `NavigateToCategoryTvShows should be equal when ids are same`() {
        val effect1 = EpisodeDetailsScreenEffect.NavigateToCategoryTvShows(CATEGORY_ID)
        val effect2 = EpisodeDetailsScreenEffect.NavigateToCategoryTvShows(CATEGORY_ID)

        assertThat(effect1).isEqualTo(effect2)
    }

    @Test
    fun `NavigateToCategoryTvShows should create new instance with updated id when copy is called`() {
        val originalEffect = EpisodeDetailsScreenEffect.NavigateToCategoryTvShows(123L)
        val copiedEffect = originalEffect.copy(categoryId = 456L)

        assertThat(copiedEffect.categoryId).isEqualTo(456L)
        assertThat(originalEffect.categoryId).isEqualTo(123L)
    }

    @Test
    fun `NavigateToActorDetails should store actorId correctly when created`() {
        val effect = EpisodeDetailsScreenEffect.NavigateToActorDetails(ACTOR_ID)

        assertThat(effect.actorId).isEqualTo(ACTOR_ID)
    }

    @Test
    fun `NavigateToActorDetails should not be equal when actorIds are different`() {
        val effect1 = EpisodeDetailsScreenEffect.NavigateToActorDetails(123L)
        val effect2 = EpisodeDetailsScreenEffect.NavigateToActorDetails(456L)

        assertThat(effect1).isNotEqualTo(effect2)
    }

    @Test
    fun `NavigateToActorDetails should create new instance with updated id when copy is called`() {
        val originalEffect = EpisodeDetailsScreenEffect.NavigateToActorDetails(123L)
        val copiedEffect = originalEffect.copy(actorId = 456L)

        assertThat(copiedEffect.actorId).isEqualTo(456L)
        assertThat(originalEffect.actorId).isEqualTo(123L)
    }

    @Test
    fun `NavigateBack should be instance of EpisodeDetailsScreenEffect when created`() {
        val effect = EpisodeDetailsScreenEffect.NavigateBack
        assertThat(effect).isInstanceOf(EpisodeDetailsScreenEffect::class.java)
    }

    @Test
    fun `NavigateBack should be instance of BaseUiEffect when created`() {
        val effect = EpisodeDetailsScreenEffect.NavigateBack
        assertThat(effect).isInstanceOf(BaseUiEffect::class.java)
    }

    @Test
    fun `NavigateToLogin should be instance of EpisodeDetailsScreenEffect when created`() {
        val effect = EpisodeDetailsScreenEffect.NavigateToLogin
        assertThat(effect).isInstanceOf(EpisodeDetailsScreenEffect::class.java)
    }

    @Test
    fun `NavigateToLogin should be instance of BaseUiEffect when created`() {
        val effect = EpisodeDetailsScreenEffect.NavigateToLogin
        assertThat(effect).isInstanceOf(BaseUiEffect::class.java)
    }

    @Test
    fun `all effects should be usable in collections when added`() {
        val effects = listOf(
            EpisodeDetailsScreenEffect.NavigateBack,
            EpisodeDetailsScreenEffect.NavigateToLogin,
            EpisodeDetailsScreenEffect.NavigateToCategoryTvShows(CATEGORY_ID),
            EpisodeDetailsScreenEffect.NavigateToActorDetails(ACTOR_ID)
        )

        assertThat(effects).hasSize(4)
        assertThat(effects).isNotEmpty()
    }

    @Test
    fun `component functions should return correct values when destructuring data classes`() {
        val navigateToCategory = EpisodeDetailsScreenEffect.NavigateToCategoryTvShows(123L)
        val (categoryIdComponent) = navigateToCategory
        assertThat(categoryIdComponent).isEqualTo(123L)

        val navigateToActor = EpisodeDetailsScreenEffect.NavigateToActorDetails(456L)
        val (actorIdComponent) = navigateToActor
        assertThat(actorIdComponent).isEqualTo(456L)
    }

    @Test
    fun `toString should include class name and id when called on effects`() {
        val navigateToCategory = EpisodeDetailsScreenEffect.NavigateToCategoryTvShows(123L)
        val navigateBack = EpisodeDetailsScreenEffect.NavigateBack
        val navigateToLogin = EpisodeDetailsScreenEffect.NavigateToLogin
        val navigateToActor = EpisodeDetailsScreenEffect.NavigateToActorDetails(456L)

        assertThat(navigateToCategory.toString()).contains("NavigateToCategoryTvShows")
        assertThat(navigateToCategory.toString()).contains("123")
        assertThat(navigateBack.toString()).isEqualTo("NavigateBack")
        assertThat(navigateToLogin.toString()).isEqualTo("NavigateToLogin")
        assertThat(navigateToActor.toString()).contains("NavigateToActorDetails")
        assertThat(navigateToActor.toString()).contains("456")
    }

    private companion object {
        const val CATEGORY_ID = 123L
        const val ACTOR_ID = 456L
    }
}