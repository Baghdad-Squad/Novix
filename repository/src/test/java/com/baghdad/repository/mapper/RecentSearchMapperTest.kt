package com.baghdad.repository.mapper

import com.baghdad.repository.model.RecentSearchDto
import com.google.common.truth.Truth.assertThat
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.junit.jupiter.api.Test
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

@OptIn(ExperimentalTime::class)
class RecentSearchMapperTest {

    @Test
    fun `should map to entity correctly when dto contains valid data`() {
        // Given
        val recentSearchDto = createMockRecentSearchDto(
            id = 1L,
            query = "action movies",
            searchedAt = 1672574400000L
        )

        // When
        val result = recentSearchDto.toEntity()

        // Then
        assertThat(result.id).isEqualTo(1L)
        assertThat(result.query).isEqualTo("action movies")
        assertThat(result.searchedAt).isEqualTo(
            Instant.fromEpochMilliseconds(1672574400000L)
                .toLocalDateTime(TimeZone.currentSystemDefault())
        )
    }

    @Test
    fun `should map to entity correctly when dto contains different queries`() {
        // Given
        val recentSearchDto1 = createMockRecentSearchDto(query = "comedy films")
        val recentSearchDto2 = createMockRecentSearchDto(query = "drama series")
        val recentSearchDto3 = createMockRecentSearchDto(query = "sci-fi")

        // When
        val result1 = recentSearchDto1.toEntity()
        val result2 = recentSearchDto2.toEntity()
        val result3 = recentSearchDto3.toEntity()

        // Then
        assertThat(result1.query).isEqualTo("comedy films")
        assertThat(result2.query).isEqualTo("drama series")
        assertThat(result3.query).isEqualTo("sci-fi")
    }

    companion object {
        private fun createMockRecentSearchDto(
            id: Long = 1L,
            query: String = "test query",
            searchedAt: Long = 1672574400000L
        ) = RecentSearchDto(
            id = id,
            query = query,
            searchedAt = searchedAt
        )
    }
} 