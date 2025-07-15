package com.baghdad.local_datasource.database.dto

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class LocalRecentSearchDtoTest {

    @Test
    fun `toDto   Happy path conversion`() {
        val local = LocalRecentSearchDto(
            id = 1L,
            query = "Inception",
            searchedAt = 1700000000000L
        )

        val dto = local.toDto()

        Assertions.assertEquals(local.id, dto.id)
        Assertions.assertEquals(local.query, dto.query)
        Assertions.assertEquals(local.searchedAt, dto.searchedAt)
    }

    @Test
    fun `toDto   Empty query`() {
        val local = LocalRecentSearchDto(
            id = 2L,
            query = "",
            searchedAt = 1700000001000L
        )

        val dto = local.toDto()

        Assertions.assertEquals("", dto.query)
        Assertions.assertEquals(2L, dto.id)
    }

    @Test
    fun `toDto   Edge timestamp`() {
        val local = LocalRecentSearchDto(
            id = 3L,
            query = "Tenet",
            searchedAt = 0L
        )

        val dto = local.toDto()

        Assertions.assertEquals(0L, dto.searchedAt)
    }

}