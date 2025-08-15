package com.baghdad.repository.mapper

import com.baghdad.domain.model.savedList.SavedMovie
import com.baghdad.repository.dummyData.DummyDataFactory.DummyDataFactory.SAVABLE_MOVIE_DTO
import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.Test


class SavedListItemMapperTest {
    @Test
    fun `should map SavableMovieDto to SavableMovie entity correctly`() {
        val excepted = SavedMovie(
            movie = SAVABLE_MOVIE_DTO.movie.toEntity(),
            isSaved = SAVABLE_MOVIE_DTO.isSaved,
            listId = SAVABLE_MOVIE_DTO.listId
        )

        val result = SAVABLE_MOVIE_DTO.toEntity()

        assertThat(result).isEqualTo(excepted)
    }

    @Test
    fun `should map movie field to Movie entity correctly`() {
        val result = SAVABLE_MOVIE_DTO.toEntity()
        assertThat(result.movie).isEqualTo(SAVABLE_MOVIE_DTO.movie.toEntity())
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