package com.baghdad.viewmodel.categoryTvShows

import com.baghdad.entity.media.Genre
import com.baghdad.entity.media.TvShow
import com.google.common.truth.Truth.assertThat
import kotlinx.datetime.LocalDate
import org.junit.jupiter.api.Test

class TvShowMapperTest {

    @Test
    fun `should return correct TvShowUiState when toUiState called`() {
        val uiState = tvShow.toUiState()

        assertThat(uiState.id).isEqualTo(101L)
        assertThat(uiState.posterPictureURL).isEqualTo("https://example.com/poster.jpg")
    }

    @Test
    fun `should return uiState with empty posterPictureURL when TvShow has empty poster url`() {
        val show = tvShow.copy(posterImageURL = "")
        val uiState = show.toUiState()

        assertThat(uiState.posterPictureURL).isEqualTo("")
    }

    @Test
    fun `should reflect that in uiState when TvShow has different id`() {
        val show = tvShow.copy(id = 999L)
        val uiState = show.toUiState()

        assertThat(uiState.id).isEqualTo(999L)
    }

    @Test
    fun `should preserve it in uiState when TvShow has long url`() {
        val longUrl = "https://example.com/super/long/path/poster.jpg"
        val show = tvShow.copy(posterImageURL = longUrl)
        val uiState = show.toUiState()

        assertThat(uiState.posterPictureURL).isEqualTo(longUrl)
    }

    private val genre = Genre(1L, "Drama")

    private val tvShow = TvShow(
        id = 101L,
        title = "Test Show",
        genres = listOf(genre),
        averageRating = 8.5,
        userRating = null,
        releaseDate = LocalDate.parse("2023-12-15"),
        overview = "Some test overview",
        posterImageURL = "https://example.com/poster.jpg",
        trailerURL = "https://example.com/trailer.mp4",
        headerImagesURLs = listOf("https://example.com/header1.jpg"),
        numberOfSeasons = 3
    )
}
