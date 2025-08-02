package com.baghdad.viewmodel.mapper

import com.baghdad.entity.media.Genre
import com.baghdad.entity.media.TvShow
import com.baghdad.viewmodel.topTvShowPicks.toUIState
import com.google.common.truth.Truth.assertThat
import kotlinx.datetime.LocalDate
import org.junit.jupiter.api.Test

class TopTvShowPicksMapperTest {

    @Test
    fun `toUIState should map TvShow to TvShowUiState correctly when it called`() {
        val result = tvShow.toUIState()

        assertThat(result.id).isEqualTo(456L)
        assertThat(result.posterPictureURL).isEqualTo("https://example.com/poster.jpg")
    }

    @Test
    fun `toUIState should handle empty posterImageURL correctly when it called`() {
        val tvShowWithEmptyPoster = tvShow.copy(posterImageURL = "")
        val result = tvShowWithEmptyPoster.toUIState()

        assertThat(result.posterPictureURL).isEmpty()
    }

    private val genre = Genre(1L, "Drama")

    private val tvShow = TvShow(
        id = 456L,
        title = "Test TV Show",
        genres = listOf(genre),
        averageRating = 9.0,
        userRating = null,
        releaseDate = LocalDate.parse("2023-02-20"),
        overview = "Test TV show overview",
        posterImageURL = "https://example.com/poster.jpg",
        trailerURL = "https://example.com/trailer.mp4",
        headerImagesURLs = listOf("https://example.com/header1.jpg"),
        numberOfSeasons = 3
    )
}