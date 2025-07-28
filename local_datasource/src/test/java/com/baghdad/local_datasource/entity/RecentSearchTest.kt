package com.baghdad.local_datasource.entity

import com.baghdad.local_datasource.roomDB.entity.RecentSearch
import com.baghdad.local_datasource.roomDB.entity.toDto
import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.Test

class RecentSearchTest {

    @Test
    fun `should create RecentSearch and check fields`() {

        assertThat(RECENT_SEARCH.id).isEqualTo(1L)
        assertThat(RECENT_SEARCH.query).isEqualTo("Game of Thrones")
        assertThat(RECENT_SEARCH.searchedAt).isGreaterThan(0L)
    }

    @Test
    fun `should map RecentSearch to RecentSearchDto`() {
        // Given
        val recentSearchDto = RECENT_SEARCH.toDto()

        // Then
        assertThat(recentSearchDto.id).isEqualTo(RECENT_SEARCH.id)
        assertThat(recentSearchDto.query).isEqualTo(RECENT_SEARCH.query)

    }

    companion object {
        val RECENT_SEARCH = RecentSearch(
            id = 1L,
            query = "Game of Thrones",
            searchedAt = 123456789L
        )
    }
}