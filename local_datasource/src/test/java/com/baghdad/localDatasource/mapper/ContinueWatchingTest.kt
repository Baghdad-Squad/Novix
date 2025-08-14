package com.baghdad.localDatasource.mapper

import com.baghdad.localDatasource.roomDB.entity.ContinueWatching
import com.baghdad.localDatasource.roomDB.entity.toDto
import com.baghdad.localDatasource.roomDB.entity.toDtos
import com.baghdad.repository.model.ContinueWatchingDto
import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test


class ContinueWatchingTest {

    @Test
    fun `should convert all fields correctly when toDto is called`() {
        // When
        val result = CONTINUE_WATCHING.toDto()

        // Then
        assertThat(result.contentId).isEqualTo(1L)
        assertThat(result.genreIds).containsExactly(1L, 2L, 3L)
        assertThat(result.contentImageUrl).isEqualTo("https://example.com/poster.jpg")
        assertThat(result.contentType).isEqualTo(ContinueWatchingDto.ContentType.MOVIE)
        assertThat(result.userId).isEqualTo(1L)
    }

    @Test
    fun `should throw exception when toDto is called with invalid contentType`() {
        // Given
        val invalidEntity = CONTINUE_WATCHING.copy(contentType = "INVALID")
        // Then
        assertThrows(IllegalArgumentException::class.java) {
            invalidEntity.toDto()
        }
    }

    @Test
    fun `should handle all enum values correctly when toDto is called`() {
        // When & Then
        assertThat(CONTINUE_WATCHING.toDto().contentType)
            .isEqualTo(ContinueWatchingDto.ContentType.MOVIE)
    }

    @Test
    fun `should return empty genreIds when toLocalDto is called with empty genreIds`() {
        // Given
        val result = CONTINUE_WATCHING.copy(genreIds = emptyList())

        // Then
        assertThat(result.genreIds).isEmpty()
    }

    @Test
    fun `should return empty contentImageUrl when toDto is called with empty contentImageUrl`() {
        // Given
        val emptyImage = CONTINUE_WATCHING.copy(contentImageUrl = "")

        // When
        val result = emptyImage.toDto()

        // Then
        assertThat(result.contentImageUrl).isEmpty()
    }

    @Test
    fun `should convert list of ContinueWatching to list of ContinueWatchingDto when toDtos is called`() {
        // Given
        val entities = listOf(
            CONTINUE_WATCHING,
            CONTINUE_WATCHING.copy(
                contentId = 2L,
                contentType = "TV_SHOW"
            )
        )

        // When
        val result = entities.toDtos()

        // Then
        assertThat(result).hasSize(2)

        assertThat(result[0].contentId).isEqualTo(1L)
        assertThat(result[0].contentType).isEqualTo(ContinueWatchingDto.ContentType.MOVIE)

        assertThat(result[1].contentId).isEqualTo(2L)
        assertThat(result[1].contentType).isEqualTo(ContinueWatchingDto.ContentType.TV_SHOW)
    }


    companion object {
        val CONTINUE_WATCHING = ContinueWatching(
            contentId = 1L,
            genreIds = listOf(1L, 2L, 3L),
            contentImageUrl = "https://example.com/poster.jpg",
            contentType = "MOVIE",
            userId = 1L
        )
    }
}
