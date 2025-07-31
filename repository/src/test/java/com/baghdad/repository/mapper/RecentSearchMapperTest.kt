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
    fun `RecentSearchDto toEntity should map correctly with valid data`() {
        // Given
        val recentSearchDto = createMockRecentSearchDto(
            id = 1L,
            query = "action movies",
            searchedAt = 1672574400000L // 2023-01-01 12:00:00 UTC
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
    fun `RecentSearchDto toEntity should handle different queries`() {
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

    @Test
    fun `RecentSearchDto toEntity should handle different IDs`() {
        // Given
        val recentSearchDto1 = createMockRecentSearchDto(id = 1L)
        val recentSearchDto2 = createMockRecentSearchDto(id = 999999L)
        val recentSearchDto3 = createMockRecentSearchDto(id = 0L)

        // When
        val result1 = recentSearchDto1.toEntity()
        val result2 = recentSearchDto2.toEntity()
        val result3 = recentSearchDto3.toEntity()

        // Then
        assertThat(result1.id).isEqualTo(1L)
        assertThat(result2.id).isEqualTo(999999L)
        assertThat(result3.id).isEqualTo(0L)
    }

    @Test
    fun `RecentSearchDto toEntity should handle different timestamps`() {
        // Given
        val timestamp1 = 1640995200000L // 2022-01-01 00:00:00 UTC
        val timestamp2 = 1704067200000L // 2024-01-01 00:00:00 UTC
        val recentSearchDto1 = createMockRecentSearchDto(
            id = 1L,
            searchedAt = timestamp1
        )
        val recentSearchDto2 = createMockRecentSearchDto(
            id = 2L,
            searchedAt = timestamp2
        )

        // When
        val result1 = recentSearchDto1.toEntity()
        val result2 = recentSearchDto2.toEntity()

        // Then
        assertThat(result1.searchedAt).isEqualTo(
            Instant.fromEpochMilliseconds(timestamp1)
                .toLocalDateTime(TimeZone.currentSystemDefault())
        )
        assertThat(result2.searchedAt).isEqualTo(
            Instant.fromEpochMilliseconds(timestamp2)
                .toLocalDateTime(TimeZone.currentSystemDefault())
        )
    }

    @Test
    fun `RecentSearchDto toEntity should handle empty query`() {
        // Given
        val recentSearchDto = createMockRecentSearchDto(query = "")

        // When
        val result = recentSearchDto.toEntity()

        // Then
        assertThat(result.query).isEmpty()
    }

    @Test
    fun `RecentSearchDto toEntity should handle single character query`() {
        // Given
        val recentSearchDto = createMockRecentSearchDto(query = "a")

        // When
        val result = recentSearchDto.toEntity()

        // Then
        assertThat(result.query).isEqualTo("a")
    }

    @Test
    fun `RecentSearchDto toEntity should handle very long query`() {
        // Given
        val longQuery = "This is a very long search query that contains many words and should be handled properly by the mapper"
        val recentSearchDto = createMockRecentSearchDto(query = longQuery)

        // When
        val result = recentSearchDto.toEntity()

        // Then
        assertThat(result.query).isEqualTo(longQuery)
    }

    @Test
    fun `RecentSearchDto toEntity should handle special characters in query`() {
        // Given
        val specialQuery = "action & adventure movies (2023)"
        val recentSearchDto = createMockRecentSearchDto(query = specialQuery)

        // When
        val result = recentSearchDto.toEntity()

        // Then
        assertThat(result.query).isEqualTo(specialQuery)
    }

    @Test
    fun `RecentSearchDto toEntity should handle unicode characters in query`() {
        // Given
        val unicodeQuery = "acción películas español"
        val recentSearchDto = createMockRecentSearchDto(query = unicodeQuery)

        // When
        val result = recentSearchDto.toEntity()

        // Then
        assertThat(result.query).isEqualTo(unicodeQuery)
    }

    @Test
    fun `RecentSearchDto toEntity should handle zero timestamp`() {
        // Given
        val recentSearchDto = createMockRecentSearchDto(searchedAt = 0L)

        // When
        val result = recentSearchDto.toEntity()

        // Then
        assertThat(result.searchedAt).isEqualTo(
            Instant.fromEpochMilliseconds(0L)
                .toLocalDateTime(TimeZone.currentSystemDefault())
        )
    }

    @Test
    fun `RecentSearchDto toEntity should handle negative timestamp`() {
        // Given
        val recentSearchDto = createMockRecentSearchDto(searchedAt = -1000L)

        // When
        val result = recentSearchDto.toEntity()

        // Then
        assertThat(result.searchedAt).isEqualTo(
            Instant.fromEpochMilliseconds(-1000L)
                .toLocalDateTime(TimeZone.currentSystemDefault())
        )
    }

    @Test
    fun `RecentSearchDto toEntity should handle very large timestamp`() {
        // Given
        val largeTimestamp = 9999999999999L
        val recentSearchDto = createMockRecentSearchDto(searchedAt = largeTimestamp)

        // When
        val result = recentSearchDto.toEntity()

        // Then
        assertThat(result.searchedAt).isEqualTo(
            Instant.fromEpochMilliseconds(largeTimestamp)
                .toLocalDateTime(TimeZone.currentSystemDefault())
        )
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