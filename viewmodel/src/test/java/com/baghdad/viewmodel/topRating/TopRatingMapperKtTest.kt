package com.baghdad.viewmodel.topRating

import com.baghdad.entity.media.Genre
import com.baghdad.home.FakeHomeScreenData
import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.Test


class TopRatingMapperKtTest {

    @Test
    fun `should return GenreUiState with valid data when toTopRatingGenreUiState is called`() {
        // Given
        val genre = Genre(1, "Action")
        // When
        val result = genre.toTopRatingGenreUiState()
        // Then
        assertThat(result.id == genre.id).isTrue()
        assertThat(result.name == genre.name).isTrue()
    }

    @Test
    fun `should return empty GenreUiState name when toTopRatingGenreUiState is called with empty name`() {
        // Given
        val genre = Genre(1, "")
        // When
        val result = genre.toTopRatingGenreUiState()
        // Then
        assertThat(result.name.isEmpty()).isTrue()
    }

    @Test
    fun `should return topRatingMovieUiState when toTopRatingMovieUiState is called`() {
        // Given
        val movie = FakeHomeScreenData.movie
        // When
        val result = movie.toTopRatingMovieUiState()
        // Then
        assertThat(result.id == movie.id).isTrue()

    }


    @Test
    fun `should return topRatingTvShowUiState when toTopRatingTvShowUiState is called`() {
        // Given
        val tvShow = FakeHomeScreenData.tvShow
        // When
        val result = tvShow.toTopRatingTvShowUiState()
        // Then
        assertThat(result.id == tvShow.id).isTrue()
    }
}