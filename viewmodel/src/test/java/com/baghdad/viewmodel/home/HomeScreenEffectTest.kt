package com.baghdad.viewmodel.home

import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.Test

class HomeScreenEffectTest {

    @Test
    fun `effect should hold correct movieId when NavigateToMovieDetails is used`() {
        val effect = HomeScreenEffect.NavigateToMovieDetails(movieId = 42L)
        assertThat(effect.movieId).isEqualTo(42L)
    }

    @Test
    fun `effect should hold correct tvShowId when NavigateToTvShowDetails is created`() {
        val effect = HomeScreenEffect.NavigateToTvShowDetails(tvShowId = 99L)
        assertThat(effect.tvShowId).isEqualTo(99L)
    }

}
