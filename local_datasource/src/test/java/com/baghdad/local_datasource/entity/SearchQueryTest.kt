package com.baghdad.local_datasource.entity

import com.baghdad.local_datasource.roomDB.entity.SearchQuery
import com.baghdad.local_datasource.roomDB.entity.toDto
import com.baghdad.repository.model.RecentlyViewedDto
import com.baghdad.repository.model.SearchQueryDto
import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class SearchQueryTest {

    @Test
    fun `should create SearchQuery with correct values when explicit values are provided`() {
        //Then
        assertThat(SEARCH_QUERY.queryName).isEqualTo("Game of Thrones")
        assertThat(SEARCH_QUERY.mediaId).isEqualTo(1L)
        assertThat(SEARCH_QUERY.mediaType).isEqualTo("TV_SHOW")
        assertThat(SEARCH_QUERY.timeStamp).isEqualTo(123456789L)
    }

    @Test
    fun `should set current timestamp when no explicit timeStamp is provided`() {
        //Then
        assertThat(SEARCH_QUERY.timeStamp).isGreaterThan(0L)
    }

    @Test
    fun `should map to SearchQueryDto when calling toDto`() {
        // Given
        val searchQueryDto = SEARCH_QUERY.toDto()

        assertThat(searchQueryDto.queryName).isEqualTo(SEARCH_QUERY.queryName)
        assertThat(searchQueryDto.mediaId).isEqualTo(SEARCH_QUERY.mediaId)
        assertThat(searchQueryDto.mediaType).isEqualTo(SearchQueryDto.MediaType.TV_SHOW)
    }

    @Test
    fun `should throw IllegalArgumentException when mediaType is invalid`() {
        // Given
        val invalidQuery = SEARCH_QUERY.copy(mediaType = "INVALID_TYPE")
        // When , Then
        assertThrows<IllegalArgumentException> {
            invalidQuery.toDto()
        }
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
