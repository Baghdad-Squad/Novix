package com.baghdad.local_datasource.database.dto

import com.baghdad.repository.model.GenreDto
import org.junit.Assert.*
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class LocalGenreDtoTest {

    @Test
    fun `toDto   Happy path`() {
        val local = LocalGenreDto(id = 1L, name = "Action", type = "MOVIE")
        val dto = local.toDto()

        Assertions.assertEquals(local.id, dto.id)
        Assertions.assertEquals(local.name, dto.name)
        Assertions.assertEquals(GenreDto.GenreType.MOVIE, dto.type)
    }

    @Test
    fun `toDto   TV_SHOW type`() {
        val local = LocalGenreDto(id = 2L, name = "Drama", type = "TV_SHOW")
        val dto = local.toDto()

        Assertions.assertEquals(GenreDto.GenreType.TV_SHOW, dto.type)
    }

    @Test
    fun `toDto   Unknown type throws exception`() {
        val local = LocalGenreDto(id = 3L, name = "Mystery", type = "DOCUMENTARY")

        assertThrows(IllegalArgumentException::class.java) {
            local.toDto()
        }
    }

    @Test
    fun `toDto   Empty type throws exception`() {
        val local = LocalGenreDto(id = 4L, name = "Romance", type = "")

        assertThrows(IllegalArgumentException::class.java) {
            local.toDto()
        }
    }

    @Test
    fun `toDto   Case-sensitive type validation`() {
        val local = LocalGenreDto(id = 5L, name = "Fantasy", type = "tv_show")

        assertThrows(IllegalArgumentException::class.java) {
            local.toDto()
        }
    }

}