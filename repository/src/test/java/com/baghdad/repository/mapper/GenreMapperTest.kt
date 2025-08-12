package com.baghdad.repository.mapper

import com.baghdad.repository.dummyData.DummyDataFactory.createMockGenreDto
import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.Test

class GenreMapperTest {
    @Test
    fun `toEntity should return Genre entity when given valid GenreDto`() {
        // Given
        val genreDto = createMockGenreDto(28L, "Action")

        // When
        val result = genreDto.toEntity()

        // Then
        assertThat(result.id).isEqualTo(28L)
        assertThat(result.name).isEqualTo("Action")
    }
} 