package com.baghdad.repository.mapper

import com.baghdad.repository.dummyData.DummyDataFactory.RECENT_SEARCH_DTO
import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.Test
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
class RecentSearchMapperTest {

    @Test
    fun `should map to entity correctly when dto contains valid data`() {
        val result = RECENT_SEARCH_DTO.toEntity()

        assertThat(result).isEqualTo(RECENT_SEARCH_DTO)
    }

    @Test
    fun `should map RecentSearchDto id to entity id correctly`() {
        val result = RECENT_SEARCH_DTO.toEntity()

        assertThat(result.id).isEqualTo(RECENT_SEARCH_DTO.id)
    }

    @Test
    fun `should map RecentSearchDto query to entity query correctly`() {
        val result = RECENT_SEARCH_DTO.toEntity()

        assertThat(result.query).isEqualTo(RECENT_SEARCH_DTO.query)
    }

    @Test
    fun `should map RecentSearchDto searchedAt to entity searchedAt correctly`() {
        val result = RECENT_SEARCH_DTO.toEntity()

        assertThat(result.searchedAt).isEqualTo(RECENT_SEARCH_DTO.searchedAt)
    }


    @Test
    fun `should map empty RecentSearchDto id to entity id correctly`() {
        val dto = RECENT_SEARCH_DTO.copy(id = 0L)
        val result = dto.toEntity()

        assertThat(result.id).isEqualTo(0L)
    }

    @Test
    fun `should map empty RecentSearchDto query to entity query correctly`() {
        val dto = RECENT_SEARCH_DTO.copy(query = "")
        val result = dto.toEntity()

        assertThat(result.query).isEqualTo("")
    }

    @Test
    fun `should map empty RecentSearchDto searchedAt to entity searchedAt correctly`() {
        val dto = RECENT_SEARCH_DTO.copy(searchedAt = 0L)
        val result = dto.toEntity()

        assertThat(result.searchedAt).isEqualTo(0L)
    }
} 