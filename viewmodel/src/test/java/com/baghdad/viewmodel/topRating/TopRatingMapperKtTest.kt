package com.baghdad.viewmodel.topRating

import com.baghdad.entity.media.Genre
import com.baghdad.home.FakeHomeScreenData
import org.junit.jupiter.api.Test


class TopRatingMapperKtTest {

    @Test
    fun `toTopRatingGenreUiState with valid Genre should return GenreUiState`() {
        // Given
        val genre = Genre(1, "Action")
        // When
        val result = genre.toTopRatingGenreUiState()
        // Then
        assert(result.id == genre.id)
        assert(result.name == genre.name)
    }

    @Test
    fun `toTopRatingGenreUiState with empty name should return GenreUiState with empty name`() {
        // Given
        val genre = Genre(1, "")
        // When
        val result = genre.toTopRatingGenreUiState()
        // Then
        assert(result.name.isEmpty())
    }

    @Test
    fun `toTopRatingMovieUiState with valid Movie should return MovieUiState`() {
        // Given
        val movie = FakeHomeScreenData.movie
        // When
        val result = movie.toTopRatingMovieUiState()
        // Then
        assert(result.id == movie.id)

    }


    @Test
    fun `toTopRatingTvShowUiState with valid TvShow should return TvShowUiState`() {
        // Given
        val tvShow = FakeHomeScreenData.tvShow
        // When
        val result = tvShow.toTopRatingTvShowUiState()
        // Then
        assert(result.id == tvShow.id)
    }
}