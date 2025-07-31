package com.baghdad.home

import com.baghdad.viewmodel.home.HomeScreenEffect
import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.Test

class HomeScreenEffectTest {

    @Test
    fun `should hold correct movieId when NavigateToMovieDetails is used`() {
        // Given
        val effect = HomeScreenEffect.NavigateToMovieDetails(movieId = 42L)

        // Then
        assertThat(effect).isInstanceOf(HomeScreenEffect.NavigateToMovieDetails::class.java)
        assert(effect.movieId == 42L)
    }

    @Test
    fun `should hold correct tvShowId when NavigateToTvShowDetails is created`() {
        // Given
        val effect = HomeScreenEffect.NavigateToTvShowDetails(tvShowId = 99L)

        // Then
        assertThat(effect).isInstanceOf(HomeScreenEffect.NavigateToTvShowDetails::class.java)
        assertThat(effect.tvShowId).isEqualTo(99L)
    }

    @Test
    fun `should be of correct type when NavigateToContinueWatching is created`() {
        // Given
        val effect = HomeScreenEffect.NavigateToContinueWatching

        // Then
        assertThat(effect).isInstanceOf(HomeScreenEffect.NavigateToContinueWatching::class.java)
    }

    @Test
    fun `should be of correct type when NavigateToTopRating is created`() {
        // Given
        val effect = HomeScreenEffect.NavigateToTopRating

        // Then
        assertThat(effect).isInstanceOf(HomeScreenEffect.NavigateToTopRating::class.java)
    }

    @Test
    fun `should be of correct type when NavigateToMovies is created`() {
        // Given
        val effect = HomeScreenEffect.NavigateToMovies

        // Then
        assertThat(effect).isInstanceOf(HomeScreenEffect.NavigateToMovies::class.java)
    }

    @Test
    fun `should be of correct type when NavigateToTvShows is created`() {
        // Given
        val effect = HomeScreenEffect.NavigateToTvShows

        // Then
        assertThat(effect).isInstanceOf(HomeScreenEffect.NavigateToTvShows::class.java)
    }

    @Test
    fun `should be of correct type when NavigateToActors is created`() {
        // Given
        val effect = HomeScreenEffect.NavigateToActors

        // Then
        assertThat(effect).isInstanceOf(HomeScreenEffect.NavigateToActors::class.java)
    }

    @Test
    fun `should be of correct type when NavigateToLogin is created`() {
        // Given
        val effect = HomeScreenEffect.NavigateToLogin

        // Then
        assertThat(effect).isInstanceOf(HomeScreenEffect.NavigateToLogin::class.java)
    }
}
