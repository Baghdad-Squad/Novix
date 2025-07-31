package com.baghdad.viewmodel.trendingTvShow

import com.baghdad.entity.media.Genre
import com.baghdad.entity.media.TvShow
import com.google.common.truth.Truth.assertThat
import kotlinx.datetime.LocalDate
import org.junit.jupiter.api.Test

class TrendingTvShowMappersTest {

    @Test
    fun `when genre mapped to ui state then should return correct GenreUiState`() {
        val genre = Genre(id = 100L, name = "Fantasy")
        val uiState = genre.toUiState()

        assertThat(uiState.id).isEqualTo(100L)
        assertThat(uiState.name).isEqualTo("Fantasy")
    }

    @Test
    fun `when genre has empty name then should return GenreUiState with empty name`() {
        val genre = Genre(id = 1L, name = "")
        val uiState = genre.toUiState()

        assertThat(uiState.id).isEqualTo(1L)
        assertThat(uiState.name).isEmpty()
    }

    @Test
    fun `when tvShow mapped to ui state then should return correct TvShowUiState`() {
        val tvShow = TvShow(
            id = 10L,
            title = "Sample Show",
            genres = emptyList(),
            averageRating = 9.0,
            userRating = null,
            releaseDate = LocalDate.parse("2023-03-15"),
            overview = "Test overview",
            posterImageURL = "poster.jpg",
            trailerURL = "trailer.mp4",
            headerImagesURLs = listOf("img1.jpg", "img2.jpg"),
            numberOfSeasons = 2
        )

        val uiState = tvShow.toUiState()

        assertThat(uiState.id).isEqualTo(10L)
        assertThat(uiState.posterPictureURL).isEqualTo("poster.jpg")
        assertThat(uiState.isSaved).isFalse()
    }

    @Test
    fun `when tvShow has empty poster URL then should return TvShowUiState with empty URL`() {
        val tvShow = TvShow(
            id = 50L,
            title = "No Poster Show",
            genres = emptyList(),
            averageRating = 5.0,
            userRating = 4.0,
            releaseDate = LocalDate.parse("2022-01-01"),
            overview = "",
            posterImageURL = "",
            trailerURL = "",
            headerImagesURLs = emptyList(),
            numberOfSeasons = 1
        )

        val uiState = tvShow.toUiState()

        assertThat(uiState.id).isEqualTo(50L)
        assertThat(uiState.posterPictureURL).isEmpty()
        assertThat(uiState.isSaved).isFalse()
    }
}
