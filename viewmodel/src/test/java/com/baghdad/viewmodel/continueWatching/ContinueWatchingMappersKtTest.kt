package com.baghdad.viewmodel.continueWatching

import com.baghdad.entity.media.Genre
import com.baghdad.home.FakeHomeScreenData
import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.Test

class ContinueWatchingMappersKtTest {

    @Test
    fun `should map Genre entity to ContinueWatchingUiState when mapping`() {
        // Given
        val genre = Genre(1L, "Action")
        // When
        val result = genre.toContinueWatchingUiState()
        // Then
        assertThat(result.id == genre.id).isTrue()
        assertThat(result.name == genre.name).isTrue()
    }

    @Test
    fun `should map ContinueWatching entity to ContinueWatchingUiState when mapping with Genre Empty Name`() {
        // Given
        val genre = Genre(1L, "")
        // When
        val result = genre.toContinueWatchingUiState()
        // Then
        assertThat(result.id == genre.id).isTrue()
        assertThat(result.name == genre.name).isTrue()
    }

    @Test
    fun `should map ContinueWatching entity to ContinueWatchingUiState when mapping`() {
        // Given
        val continueWatching = FakeHomeScreenData.continueWatchingItems[0]
        // When
        val result = continueWatching.toContinueWatchingUiState()
        // Then
        assertThat(result.id == continueWatching.contentId).isTrue()
        assertThat(result.posterPictureURL == continueWatching.contentImageUrl).isTrue()
        assertThat(result.isSaved).isTrue()
    }
}