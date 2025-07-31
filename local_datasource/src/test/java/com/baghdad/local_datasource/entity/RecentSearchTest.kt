package com.baghdad.local_datasource.entity

import com.baghdad.local_datasource.roomDB.entity.RecentSearch
import com.baghdad.local_datasource.roomDB.entity.toDto
import com.baghdad.repository.model.RecentSearchDto
import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.Test

class RecentSearchTest {

    @Test
    fun `should create RecentSearch and check explicit fields`() {
        // Then
        assertThat(RECENT_SEARCH.id).isEqualTo(1L)
        assertThat(RECENT_SEARCH.query).isEqualTo("Game of Thrones")
        assertThat(RECENT_SEARCH.searchedAt).isEqualTo(123456789L)
    }

    @Test
    fun `should create RecentSearch with default searchedAt when not provided`() {
        // When
        val recentSearch = RecentSearch(
            id = 2L,
            query = "Breaking Bad"
        )

        // Then
        assertThat(recentSearch.id).isEqualTo(2L)
        assertThat(recentSearch.query).isEqualTo("Breaking Bad")
        assertThat(recentSearch.searchedAt).isGreaterThan(0L)
    }

    @Test
    fun `should map RecentSearch to RecentSearchDto correctly`() {
        // When
        val dto = RECENT_SEARCH.toDto()

        // Then
        assertThat(dto).isEqualTo(
            RecentSearchDto(
                id = RECENT_SEARCH.id,
                query = RECENT_SEARCH.query,
                searchedAt = RECENT_SEARCH.searchedAt
            )
        )
    }

    companion object {
        val RECENT_SEARCH = RecentSearch(
            id = 1L,
            query = "Game of Thrones",
            searchedAt = 123456789L
        )
    }
}
