package com.baghdad.viewmodel.trendingTvShow

import com.baghdad.entity.media.Genre
import com.baghdad.viewmodel.topTvShowPicks.MockTvShow
import com.baghdad.viewmodel.trendingTvShow.TrendingTvShowScreenState.GenreUiState
import com.google.common.truth.Truth.assertThat
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
        val tvShow = MockTvShow.TV_SHOW
        val expectedTvShow = TrendingTvShowScreenState.TvShowUiState(
            id = tvShow.id,
            posterPictureURL = tvShow.posterImageURL,
        )

        val uiState = tvShow.toUiState()

        assertThat(uiState).isEqualTo(expectedTvShow)
    }
}