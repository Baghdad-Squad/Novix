package com.baghdad.local_datasource.entity

import com.baghdad.local_datasource.roomDB.entity.SearchQuery
import com.baghdad.local_datasource.roomDB.entity.toDto
import com.baghdad.local_datasource.roomDB.entity.toLocalDto
import com.baghdad.repository.model.RecentlyViewedDto
import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.Test

class SearchQueryTest {

    @Test
    fun `should create SearchQuery and check fields`() {
        // Then
        assertThat(SEARCH_QUERY.queryName).isEqualTo("Game of Thrones")
        assertThat(SEARCH_QUERY.mediaId).isEqualTo(1L)
        assertThat(SEARCH_QUERY.mediaType).isEqualTo("TV_SHOW")
        assertThat(SEARCH_QUERY.timeStamp).isGreaterThan(0L)
    }

    @Test
    fun `should map SearchQuery to SearchQueryDto`() {
        // When
        val searchQueryDto = SEARCH_QUERY.toDto()

        // Then
        assertThat(searchQueryDto.queryName).isEqualTo(SEARCH_QUERY.queryName)
        assertThat(searchQueryDto.mediaId).isEqualTo(SEARCH_QUERY.mediaId)
    }

    @Test
    fun `should map SearchQueryDto to SearchQuery`() {
        // Given
        val searchQueryDto = SEARCH_QUERY.toDto()

        // When
        val searchQuery = searchQueryDto.toLocalDto()

        // Then
        assertThat(searchQuery.queryName).isEqualTo(searchQueryDto.queryName)
        assertThat(searchQuery.mediaId).isEqualTo(searchQueryDto.mediaId)
        assertThat(searchQuery.mediaType).isEqualTo(searchQueryDto.mediaType.name)
    }

    companion object {
        val SEARCH_QUERY = SearchQuery(
            queryName = "Game of Thrones",
            mediaId = 1L,
            mediaType = RecentlyViewedDto.ContentType.TV_SHOW.name,
            timeStamp = 123456789L
        )
    }
}