package com.baghdad.repository.mapper

import com.baghdad.repository.model.GenreDto
import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.Test

class GenreMapperTest {

    @Test
    fun `should map correctly to entity when GenreDto has valid data`() {
        // Given
        val genreDto = createMockGenreDto(28L, "Action")

        // When
        val result = genreDto.toEntity()

        // Then
        assertThat(result.id).isEqualTo(28L)
        assertThat(result.name).isEqualTo("Action")
    }

    @Test
    fun `should map correctly to entity list when list of GenreDto is provided`() {
        // Given
        val genreDtos = listOf(
            createMockGenreDto(28L, "Action"),
            createMockGenreDto(35L, "Comedy"),
            createMockGenreDto(18L, "Drama")
        )

        // When
        val result = genreDtos.toEntities()

        // Then
        assertThat(result.size).isEqualTo(3)
        assertThat(result[0].id).isEqualTo(28L)
        assertThat(result[0].name).isEqualTo("Action")
        assertThat(result[1].id).isEqualTo(35L)
        assertThat(result[1].name).isEqualTo("Comedy")
        assertThat(result[2].id).isEqualTo(18L)
        assertThat(result[2].name).isEqualTo("Drama")
    }

    @Test
    fun `should return empty list when mapping empty list of GenreDto to entities`() {
        // Given
        val genreDtos = emptyList<GenreDto>()

        // When
        val result = genreDtos.toEntities()

        // Then
        assertThat(result).isEmpty()
    }

    @Test
    fun `should map correctly to entity list when GenreDto list contains one item`() {
        // Given
        val genreDtos = listOf(createMockGenreDto(28L, "Action"))

        // When
        val result = genreDtos.toEntities()

        // Then
        assertThat(result.size).isEqualTo(1)
        assertThat(result[0].id).isEqualTo(28L)
        assertThat(result[0].name).isEqualTo("Action")
    }

    companion object {
        private fun createMockGenreDto(id: Long, name: String) = GenreDto(
            id = id,
            name = name,
            type = GenreDto.GenreType.MOVIE
        )
    }
} 