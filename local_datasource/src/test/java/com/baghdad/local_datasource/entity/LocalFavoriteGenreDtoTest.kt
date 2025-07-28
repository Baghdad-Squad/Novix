package com.baghdad.local_datasource.entity

import com.baghdad.local_datasource.roomDB.entity.FavoriteGenre
import com.baghdad.local_datasource.roomDB.entity.toDto
import com.google.common.truth.Truth
import org.junit.jupiter.api.Test

class LocalFavoriteGenreDtoTest {

    @Test
    fun `should create LocalFavoriteGenreDto and check fields`() {
        // Then
        Truth.assertThat(LOCAL_FAVORITE_GENRE_DTO.genreId).isEqualTo(1L)
        Truth.assertThat(LOCAL_FAVORITE_GENRE_DTO.name).isEqualTo("Action")
        Truth.assertThat(LOCAL_FAVORITE_GENRE_DTO.count).isEqualTo(5)
        Truth.assertThat(LOCAL_FAVORITE_GENRE_DTO.timeStamp).isEqualTo(123456789L)
    }

    @Test
    fun `should show default values are applied correctly`() {
        // Given
        val dto = FavoriteGenre(
            genreId = 2L,
            name = "Comedy"
        )

        // Then
        Truth.assertThat(dto.genreId).isEqualTo(2L)
        Truth.assertThat(dto.name).isEqualTo("Comedy")
        Truth.assertThat(dto.count).isEqualTo(0)
        Truth.assertThat(dto.timeStamp).isGreaterThan(0L)
    }

    @Test
    fun `should create LocalFavoriteGenreDto from DTO`() {
        // When
        val result = LOCAL_FAVORITE_GENRE_DTO.toDto()

        // Then
        Truth.assertThat(result.genreId).isEqualTo(LOCAL_FAVORITE_GENRE_DTO.genreId)
        Truth.assertThat(result.name).isEqualTo(LOCAL_FAVORITE_GENRE_DTO.name)
        Truth.assertThat(result.count).isEqualTo(LOCAL_FAVORITE_GENRE_DTO.count)
        Truth.assertThat(result.timeStamp).isEqualTo(LOCAL_FAVORITE_GENRE_DTO.timeStamp)
    }

    companion object {
        val LOCAL_FAVORITE_GENRE_DTO = FavoriteGenre(
            genreId = 1L,
            name = "Action",
            count = 5,
            timeStamp = 123456789L
        )
    }
}