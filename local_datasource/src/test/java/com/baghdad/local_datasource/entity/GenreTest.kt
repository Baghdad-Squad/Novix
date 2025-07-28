package com.baghdad.local_datasource.entity

import com.baghdad.local_datasource.roomDB.entity.Genre
import com.baghdad.local_datasource.roomDB.entity.toDto
import com.baghdad.repository.model.GenreDto
import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test

class GenreTest {

    @Test
    fun `toDto handles minimum long value for id`() {
        // Given
        val genre = Genre(
            id = Long.MIN_VALUE,
            name = "Min Value Genre",
            type = "MOVIE"
        )

        // When
        val result = genre.toDto()

        // Then
        assertThat(result.id).isEqualTo(Long.MIN_VALUE)
        assertThat(result.name).isEqualTo("Min Value Genre")
        assertThat(result.type).isEqualTo(GenreDto.GenreType.MOVIE)
    }

    @Test
    fun `toDto handles maximum long value for id`() {
        // Given
        val genre = Genre(
            id = Long.MAX_VALUE,
            name = "Max Value Genre",
            type = "TV_SHOW"
        )

        // When
        val result = genre.toDto()

        // Then
        assertThat(result.id).isEqualTo(Long.MAX_VALUE)
        assertThat(result.name).isEqualTo("Max Value Genre")
        assertThat(result.type).isEqualTo(GenreDto.GenreType.TV_SHOW)
    }

    @Test
    fun `toDto handles zero id value`() {
        // Given
        val genre = Genre(
            id = 0L,
            name = "Zero ID Genre",
            type = "MOVIE"
        )

        // When
        val result = genre.toDto()

        // Then
        assertThat(result.id).isEqualTo(0L)
    }

    @Test
    fun `toDto handles negative id values`() {
        // Given
        val genre = Genre(
            id = -12345L,
            name = "Negative ID Genre",
            type = "TV_SHOW"
        )

        // When
        val result = genre.toDto()

        // Then
        assertThat(result.id).isEqualTo(-12345L)
    }

    @Test
    fun `toDto preserves id when other fields are empty`() {
        // Given
        val genre = Genre(
            id = 999L,
            name = "",
            type = "MOVIE"
        )

        // When
        val result = genre.toDto()

        // Then
        assertThat(result.id).isEqualTo(999L)
        assertThat(result.name).isEmpty()
    }

    @Test
    fun `toDto handles id with same value as other fields`() {
        // Given
        val genre = Genre(
            id = "1".toLong(),
            name = "1",
            type = "MOVIE"
        )

        // When
        val result = genre.toDto()

        // Then
        assertThat(result.id).isEqualTo(1L)
        assertThat(result.name).isEqualTo("1")
    }

    @Test
    fun `toDto maintains exact id value across conversions`() {
        // Given
        val originalId = 987654321098765432L
        val genre = Genre(
            id = originalId,
            name = "Exact ID Test",
            type = "TV_SHOW"
        )

        // When
        val dto = genre.toDto()
        val reconstructedGenre = Genre(
            id = dto.id,
            name = dto.name,
            type = dto.type.name
        )

        // Then
        assertThat(reconstructedGenre.id).isEqualTo(originalId)
    }

    @Test
    fun `toDto converts Genre to GenreDto with all fields`() {
        // Given
        val genre = Genre(
            id = 1L,
            name = "Action",
            type = "MOVIE"
        )

        // When
        val result = genre.toDto()

        // Then
        assertThat(result).isEqualTo(
            GenreDto(
                id = 1L,
                name = "Action",
                type = GenreDto.GenreType.MOVIE
            )
        )
    }

    @Test
    fun `toDto handles all GenreType values correctly`() {
        // Given
        val movieGenre = Genre(
            id = 1L,
            name = "Movie",
            type = "MOVIE"
        )

        val tvGenre = Genre(
            id = 2L,
            name = "TV",
            type = "TV_SHOW"
        )

        // When
        val movieResult = movieGenre.toDto()
        val tvResult = tvGenre.toDto()

        // Then
        assertThat(movieResult.type).isEqualTo(GenreDto.GenreType.MOVIE)
        assertThat(tvResult.type).isEqualTo(GenreDto.GenreType.TV_SHOW)
    }

    @Test
    fun `toDto throws IllegalArgumentException for invalid GenreType`() {
        // Given
        val invalidGenre = Genre(
            id = 3L,
            name = "Invalid",
            type = "INVALID_TYPE"
        )

        // When & Then
        assertThrows(IllegalArgumentException::class.java) {
            invalidGenre.toDto()
        }
    }

    @Test
    fun `toDto preserves id and name fields exactly`() {
        // Given
        val genre = Genre(
            id = 42L,
            name = "Science Fiction",
            type = "MOVIE"
        )

        // When
        val result = genre.toDto()

        // Then
        assertThat(result.id).isEqualTo(42L)
        assertThat(result.name).isEqualTo("Science Fiction")
    }

    @Test
    fun `toDto handles case sensitivity for GenreType`() {
        // Given
        val lowercaseGenre = Genre(
            id = 4L,
            name = "Lowercase",
            type = "movie"  // lowercase
        )

        // When & Then
        assertThrows(IllegalArgumentException::class.java) {
            lowercaseGenre.toDto()
        }
    }

    @Test
    fun `toDto handles empty name field`() {
        // Given
        val emptyNameGenre = Genre(
            id = 5L,
            name = "",
            type = "MOVIE"
        )

        // When
        val result = emptyNameGenre.toDto()

        // Then
        assertThat(result.name).isEmpty()
    }

    @Test
    fun `toDto handles boundary id values`() {
        // Given
        val maxLongGenre = Genre(
            id = Long.MAX_VALUE,
            name = "Max",
            type = "MOVIE"
        )

        val minLongGenre = Genre(
            id = Long.MIN_VALUE,
            name = "Min",
            type = "TV_SHOW"
        )

        // When
        val maxResult = maxLongGenre.toDto()
        val minResult = minLongGenre.toDto()

        // Then
        assertThat(maxResult.id).isEqualTo(Long.MAX_VALUE)
        assertThat(minResult.id).isEqualTo(Long.MIN_VALUE)
    }
}