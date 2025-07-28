package com.baghdad.local_datasource.entity

import com.baghdad.local_datasource.roomDB.entity.ContinueWatching
import com.baghdad.local_datasource.roomDB.entity.toDto
import com.baghdad.local_datasource.roomDB.entity.toLocalDto
import com.baghdad.repository.model.ContinueWatchingDto
import org.junit.jupiter.api.Test
import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.Assertions.assertThrows


class ContinueWatchingTest {

    @Test
    fun `toDto converts all fields correctly`() {
        // When
        val result = fakeEntity1.toDto()

        // Then
        assertThat(result).isEqualTo(
            ContinueWatchingDto(
                contentId = 1L,
                genreIds = listOf(1L, 2L, 3L),
                contentImageUrl = "https://example.com/poster.jpg",
                contentType = ContinueWatchingDto.ContentType.MOVIE,
                userId = 1L
            )
        )
    }

    @Test
    fun `toLocalDto converts all fields correctly`() {
        // When
        val result = fakeDto.toLocalDto()

        // Then
        assertThat(result).isEqualTo(
            ContinueWatching(
                contentId = 1L,
                genreIds = listOf(4L, 5L),
                contentImageUrl = "https://example.com/tv_poster.jpg",
                contentType = "TV_SHOW",
                userId = 1L
            )
        )
    }

    @Test
    fun `conversion is bidirectional`() {
        // Given
        val original = ContinueWatching(
            contentId = 1L,
            genreIds = listOf(1L, 2L),
            contentImageUrl = "image.jpg",
            contentType = "TV_SHOW",
            userId = 1L
        )

        // When
        val converted = original.toDto().toLocalDto()

        // Then
        assertThat(converted).isEqualTo(original)
    }

    @Test
    fun `toDto throws for invalid contentType`() {
        // Given
        val invalidEntity = ContinueWatching(
            contentId = 1L,
            genreIds = emptyList(),
            contentImageUrl = "https://example.com/invalid.jpg",
            contentType = "INVALID_TYPE",
            userId = 1L
        )
        // Then
        assertThrows(IllegalArgumentException::class.java) {
            invalidEntity.toDto()
        }
    }

    @Test
    fun `handles all enum values correctly`() {
        // When & Then
        assertThat(fakeEntity1.toDto().contentType)
            .isEqualTo(ContinueWatchingDto.ContentType.MOVIE)
        assertThat(fakeEntity2.toDto().contentType)
            .isEqualTo(ContinueWatchingDto.ContentType.TV_SHOW)
    }

    @Test
    fun `handles empty genreIds`() {
        // Given
        val emptyGenres = ContinueWatchingDto(
            contentId = 3L,
            genreIds = emptyList(),
            contentImageUrl = "https://example.com/empty.jpg",
            contentType = ContinueWatchingDto.ContentType.MOVIE,
            userId = 3L
        )

        // When
        val result = emptyGenres.toLocalDto()

        // Then
        assertThat(result.genreIds).isEmpty()
    }

    @Test
    fun `handles null contentImageUrl`() {
        // Given
        val emptyImage = ContinueWatching(
            contentId = 4L,
            genreIds = listOf(1L),
            contentImageUrl = "",
            contentType = "MOVIE",
            userId = 4L
        )

        // When
        val result = emptyImage.toDto()

        // Then
        assertThat(result.contentImageUrl).isEmpty()
    }

    val fakeEntity1 = ContinueWatching(
        contentId = 1L,
        genreIds = listOf(1L, 2L, 3L),
        contentImageUrl = "https://example.com/poster.jpg",
        contentType = "MOVIE",
        userId = 1L
    )
    val fakeEntity2 = ContinueWatching(
        contentId = 2L,
        genreIds = emptyList(),
        contentImageUrl = "https://example.com/tv.jpg",
        contentType = "TV_SHOW",
        userId = 2L
    )
    val fakeDto = ContinueWatchingDto(
        contentId = 1L,
        genreIds = listOf(4L, 5L),
        contentImageUrl = "https://example.com/tv_poster.jpg",
        contentType = ContinueWatchingDto.ContentType.TV_SHOW,
        userId = 1L
    )

}