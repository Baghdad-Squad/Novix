package com.baghdad.viewmodel.categoryTvShows

import com.baghdad.viewmodel.topTvShowPicks.MockTvShow
import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.Test

class TvShowMapperTest {

    @Test
    fun `should return correct TvShowUiState when toUiState called`() {

        val tvShow = MockTvShow.TV_SHOW
        val expectedUiState = CategoryTvShowsState.TvShowUiState(
            id = tvShow.id,
            posterPictureURL = tvShow.posterImageURL
        )

        val uiState = tvShow.toUiState()

        assertThat(uiState).isEqualTo(expectedUiState)
    }
}