package com.baghdad.viewmodel.continueWatching

import com.baghdad.entity.media.Genre
import com.baghdad.home.FakeHomeScreenData
import org.junit.jupiter.api.Test

class ContinueWatchingMappersKtTest {

    @Test
    fun `toContinueWatchingUiState Genre Basic Mapping`() {
        // Given
        val genre = Genre(1L, "Action")
        // When
        val result = genre.toContinueWatchingUiState()
        // Then
        assert(result.id == genre.id)
        assert(result.name == genre.name)
    }

    @Test
    fun `toContinueWatchingUiState  Genre Empty Name`() {
        // Given
        val genre = Genre(1L, "")
        // When
        val result = genre.toContinueWatchingUiState()
        // Then
        assert(result.id == genre.id)
        assert(result.name == genre.name)
    }

    @Test
    fun `toContinueWatchingUiState ContinueWatching Basic Mapping`() {
        // Given
        val continueWatching = FakeHomeScreenData.continueWatchingItems[0]
        // When
        val result = continueWatching.toContinueWatchingUiState()
        // Then
        assert(result.id == continueWatching.contentId)
        assert(result.posterPictureURL == continueWatching.contentImageUrl)
        assert(result.isSaved)
    }
}