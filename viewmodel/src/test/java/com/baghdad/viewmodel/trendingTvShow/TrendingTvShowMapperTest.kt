package com.baghdad.viewmodel.trendingTvShow

import com.baghdad.entity.media.Genre
import com.baghdad.entity.media.TvShow
import com.baghdad.viewmodel.trendingTvShow.TrendingTvShowScreenState.GenreUiState
import com.google.common.truth.Truth.assertThat
import kotlinx.datetime.LocalDate
import org.junit.jupiter.api.Test

class TrendingTvShowMappersTest {
    @Test
    fun `toUiState should return correct GenreUiState when genre mapped`() {
        val genre = Genre(id = 22, name = "Fantasy")
        val expectedGenre = GenreUiState(id = 22, name = "Fantasy")

        val uiState = genre.toUiState()

        assertThat(uiState).isEqualTo(expectedGenre)
    }

    @Test
    fun `toUiState should return correct TvShowUiState when tv show mapped`() {
        val tvShow = TvShow(
            id = 22L,
            title = "Test Show",
            genres = listOf(Genre(id = 1L, name = "Action")),
            averageRating = 8.5,
            userRating = 7,
            releaseDate = LocalDate.parse("2002-02-22"),
            overview = "test overview",
            posterImageURL = "poster.jpg",
            trailerURL = "trailer.mp4",
            headerImagesURLs = listOf("header1.jpg", "header2.jpg"),
            numberOfSeasons = 3
        )
        val expectedTvShow = TrendingTvShowScreenState.TvShowUiState(
            id = 22L,
            posterPictureURL = "poster.jpg"
        )

        val uiState = tvShow.toUiState()

        assertThat(uiState).isEqualTo(expectedTvShow)
    }
}