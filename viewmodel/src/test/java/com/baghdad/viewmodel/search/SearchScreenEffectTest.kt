package com.baghdad.viewmodel.search

import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.Test

class SearchScreenEffectTest {

    @Test
    fun `NavigateToMovieDetails should contain correct movieId`() {
        val effect = SearchScreenEffect.NavigateToMovieDetails(movieId = 1L)

        assertThat(effect.movieId).isEqualTo(1L)
    }

    @Test
    fun `NavigateToTvShowDetails should contain correct tvShowId`() {
        val effect = SearchScreenEffect.NavigateToTvShowDetails(tvShowId = 1L)

        assertThat(effect.tvShowId).isEqualTo(1L)
    }

    @Test
    fun `NavigateToActorDetails should contain correct actorId`() {
        val effect = SearchScreenEffect.NavigateToActorDetails(actorId = 1L)

        assertThat(effect.actorId).isEqualTo(1L)
    }
}