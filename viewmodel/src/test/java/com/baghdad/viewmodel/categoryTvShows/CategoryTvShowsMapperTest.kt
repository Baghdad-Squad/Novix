package com.baghdad.viewmodel.categoryTvShows

import com.baghdad.entity.media.Genre
import com.baghdad.entity.media.TvShow
import com.google.common.truth.Truth.assertThat
import kotlinx.datetime.LocalDate
import org.junit.jupiter.api.Test

class TvShowMapperTest {

    @Test
    fun `should return correct TvShowUiState when toUiState called`() {
        val tvShow = TvShow(
            id = 101L,
            title = "Test Show",
            genres = listOf(Genre(1L, "Drama")),
            averageRating = 8.5,
            userRating = null,
            releaseDate = LocalDate.parse("2023-12-15"),
            overview = "Some test overview",
            posterImageURL = "https://example.com/poster.jpg",
            trailerURL = "https://example.com/trailer.mp4",
            headerImagesURLs = listOf("https://example.com/header1.jpg"),
            numberOfSeasons = 3
        )
        val expectedUiState = CategoryTvShowsState.TvShowUiState(
            id = tvShow.id,
            posterPictureURL = tvShow.title
        )

        val uiState = tvShow.toUiState()

        assertThat(uiState).isEqualTo(expectedUiState)
    }
}
