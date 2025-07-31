package com.baghdad.home

import com.baghdad.viewmodel.home.HomeScreenEffect
import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.Test

class HomeScreenEffectTest {

    @Test
    fun `NavigateToMovieDetails should hold correct movieId`() {
        // Given
        val effect = HomeScreenEffect.NavigateToMovieDetails(movieId = 42L)

        // Then
        assertThat(effect).isInstanceOf(HomeScreenEffect.NavigateToMovieDetails::class.java)
        assert(effect.movieId == 42L)
    }

    @Test
    fun `NavigateToTvShowDetails should hold correct tvShowId`() {
        // Given
        val effect = HomeScreenEffect.NavigateToTvShowDetails(tvShowId = 99L)

        // Then
        assertThat(effect).isInstanceOf(HomeScreenEffect.NavigateToTvShowDetails::class.java)
        assertThat(effect.tvShowId).isEqualTo(99L)
    }

    @Test
    fun `NavigateToContinueWatching should be correct type`() {
        // Given
        val effect = HomeScreenEffect.NavigateToContinueWatching

        // Then
        assertThat(effect).isInstanceOf(HomeScreenEffect.NavigateToContinueWatching::class.java)
    }

    @Test
    fun `NavigateToTopRating should be correct type`() {
        // Given
        val effect = HomeScreenEffect.NavigateToTopRating

        // Then
        assertThat(effect).isInstanceOf(HomeScreenEffect.NavigateToTopRating::class.java)
    }

    @Test
    fun `NavigateToMovies should be correct type`() {
        // Given
        val effect = HomeScreenEffect.NavigateToMovies

        // Then
        assertThat(effect).isInstanceOf(HomeScreenEffect.NavigateToMovies::class.java)
    }

    @Test
    fun `NavigateToTvShows should be correct type`() {
        // Given
        val effect = HomeScreenEffect.NavigateToTvShows

        // Then
        assertThat(effect).isInstanceOf(HomeScreenEffect.NavigateToTvShows::class.java)
    }

    @Test
    fun `NavigateToActors should be correct type`() {
        // Given
        val effect = HomeScreenEffect.NavigateToActors

        // Then
        assertThat(effect).isInstanceOf(HomeScreenEffect.NavigateToActors::class.java)
    }

    @Test
    fun `NavigateToLogin should be correct type`() {
        // Given
        val effect = HomeScreenEffect.NavigateToLogin

        // Then
        assertThat(effect).isInstanceOf(HomeScreenEffect.NavigateToLogin::class.java)
    }
}
