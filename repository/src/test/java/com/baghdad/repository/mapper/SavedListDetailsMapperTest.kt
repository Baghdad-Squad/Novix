package com.baghdad.repository.mapper

import com.baghdad.repository.dummyData.DummyDataFactory.SAVABLE_MOVIE_DTO
import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.Test


class SavedListDetailsMapperTest {
    @Test
    fun `should map SavableMovieDto to SavableMovie entity correctly`() {
        val result = SAVABLE_MOVIE_DTO.toEntity()

        assertThat(result).isEqualTo(SAVABLE_MOVIE_DTO)
    }

    @Test
    fun `should map movie field to Movie entity correctly`() {
        val result = SAVABLE_MOVIE_DTO.toEntity()
        assertThat(result.movie).isEqualTo(SAVABLE_MOVIE_DTO.toEntity())
    }

    @Test
    fun `should map isSaved to entity isSaved correctly`() {
        val result = SAVABLE_MOVIE_DTO.toEntity()
        assertThat(result.isSaved).isEqualTo(SAVABLE_MOVIE_DTO.isSaved)
    }

    @Test
    fun `should map listId to entity listId correctly`() {
        val result = SAVABLE_MOVIE_DTO.toEntity()
        assertThat(result.listId).isEqualTo(SAVABLE_MOVIE_DTO.listId)
    }
}