package com.baghdad.local_datasource.entity

import com.baghdad.local_datasource.roomDB.entity.RecentSearch
import com.baghdad.local_datasource.roomDB.entity.toDto
import com.baghdad.repository.model.RecentSearchDto
import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.Test

class RecentSearchTest {

    @Test
    fun `should create RecentSearch with correct values when instantiated`() {
        // Given
        val recentSearch = RECENT_SEARCH

        // Then
        assertThat(recentSearch.id).isEqualTo(1L)
        assertThat(recentSearch.query).isEqualTo("Game of Thrones")
        assertThat(recentSearch.searchedAt).isEqualTo(123456789L)
    }

    @Test
    fun `should map to RecentSearchDto when toDto is called`() {
        // Given
        val entity = RECENT_SEARCH

        // When
        val dto = entity.toDto()

        // Then
        assertThat(dto).isInstanceOf(RecentSearchDto::class.java)
        assertThat(dto.id).isEqualTo(entity.id)
        assertThat(dto.query).isEqualTo(entity.query)
        assertThat(dto.searchedAt).isEqualTo(entity.searchedAt)
    }

    companion object {
        val RECENT_SEARCH = RecentSearch(
            id = 1L,
            query = "Game of Thrones",
            searchedAt = 123456789L
        )
    }
}
