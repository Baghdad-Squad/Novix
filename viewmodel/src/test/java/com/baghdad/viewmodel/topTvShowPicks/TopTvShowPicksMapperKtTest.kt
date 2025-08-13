package com.baghdad.viewmodel.topTvShowPicks

import com.baghdad.viewmodel.topTvShowPicks.MockTvShow.TV_SHOW
import com.baghdad.viewmodel.topTvShowPicks.MockTvShow.TV_SHOWS
import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.Test

class TopTvShowPicksMapperKtTest {

    @Test
    fun `should map TvShow to TvShowUiState when toUIState called`() {
        // When
        val mappedState = TV_SHOW.toUIState()

        // Then
        assertThat(mappedState.id).isEqualTo(TV_SHOW.id)
        assertThat(mappedState.posterPictureURL).isEqualTo(TV_SHOW.posterImageURL)
    }

    @Test
    fun `should map TvShows to TvShowUiStates when toUIStateList called`() {
        // When
        val mappedStates = TV_SHOWS.toUIStateList()

        // Then
        assertThat(mappedStates.size).isEqualTo(TV_SHOWS.size)
        assertThat(mappedStates[0].id).isEqualTo(TV_SHOWS[0].id)
        assertThat(mappedStates[0].posterPictureURL).isEqualTo(TV_SHOWS[0].posterImageURL)
    }
}