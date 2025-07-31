package com.baghdad.repository.mapper

import com.baghdad.domain.model.search.RecentlyViewed
import com.baghdad.repository.model.RecentlyViewedDto
import com.google.common.truth.Truth.assertThat
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.junit.jupiter.api.Test
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

@OptIn(ExperimentalTime::class)
class RecentlyViewedMapperTest {

    @Test
    fun `RecentlyViewedDto toEntity should map correctly with valid data`() {
        // Given
        val recentlyViewedDto = createMockRecentlyViewedDto(
            contentId = 123L,
            contentImageUrl = "/image123.jpg",
            contentType = RecentlyViewedDto.ContentType.MOVIE,
            viewedAtEpochMillis = 1672574400000L // 2023-01-01 12:00:00 UTC
        )

        // When
        val result = recentlyViewedDto.toEntity()

        // Then
        assertThat(result.contentId).isEqualTo(123L)
        assertThat(result.contentImageUrl).isEqualTo("/image123.jpg")
        assertThat(result.contentType).isEqualTo(RecentlyViewed.ContentType.MOVIE)
        assertThat(result.viewedAt).isEqualTo(
            Instant.fromEpochMilliseconds(1672574400000L)
                .toLocalDateTime(TimeZone.currentSystemDefault())
        )
    }

    @Test
    fun `RecentlyViewedDto toEntity should handle TV_SHOW content type`() {
        // Given
        val recentlyViewedDto = createMockRecentlyViewedDto(
            contentId = 456L,
            contentImageUrl = "/image456.jpg",
            contentType = RecentlyViewedDto.ContentType.TV_SHOW,
            viewedAtEpochMillis = 1672574400000L
        )

        // When
        val result = recentlyViewedDto.toEntity()

        // Then
        assertThat(result.contentId).isEqualTo(456L)
        assertThat(result.contentImageUrl).isEqualTo("/image456.jpg")
        assertThat(result.contentType).isEqualTo(RecentlyViewed.ContentType.TV_SHOW)
    }

    @Test
    fun `RecentlyViewedDto toEntity should handle different timestamps`() {
        // Given
        val timestamp1 = 1640995200000L // 2022-01-01 00:00:00 UTC
        val timestamp2 = 1704067200000L // 2024-01-01 00:00:00 UTC
        val recentlyViewedDto1 = createMockRecentlyViewedDto(
            contentId = 1L,
            viewedAtEpochMillis = timestamp1
        )
        val recentlyViewedDto2 = createMockRecentlyViewedDto(
            contentId = 2L,
            viewedAtEpochMillis = timestamp2
        )

        // When
        val result1 = recentlyViewedDto1.toEntity()
        val result2 = recentlyViewedDto2.toEntity()

        // Then
        assertThat(result1.viewedAt).isEqualTo(
            Instant.fromEpochMilliseconds(timestamp1)
                .toLocalDateTime(TimeZone.currentSystemDefault())
        )
        assertThat(result2.viewedAt).isEqualTo(
            Instant.fromEpochMilliseconds(timestamp2)
                .toLocalDateTime(TimeZone.currentSystemDefault())
        )
    }

    @Test
    fun `RecentlyViewedDto toEntity should handle different content IDs`() {
        // Given
        val recentlyViewedDto1 = createMockRecentlyViewedDto(contentId = 1L)
        val recentlyViewedDto2 = createMockRecentlyViewedDto(contentId = 999999L)
        val recentlyViewedDto3 = createMockRecentlyViewedDto(contentId = 0L)

        // When
        val result1 = recentlyViewedDto1.toEntity()
        val result2 = recentlyViewedDto2.toEntity()
        val result3 = recentlyViewedDto3.toEntity()

        // Then
        assertThat(result1.contentId).isEqualTo(1L)
        assertThat(result2.contentId).isEqualTo(999999L)
        assertThat(result3.contentId).isEqualTo(0L)
    }

    @Test
    fun `RecentlyViewedDto toEntity should handle different image URLs`() {
        // Given
        val recentlyViewedDto1 = createMockRecentlyViewedDto(contentImageUrl = "/movie1.jpg")
        val recentlyViewedDto2 = createMockRecentlyViewedDto(contentImageUrl = "/tv_show2.png")
        val recentlyViewedDto3 = createMockRecentlyViewedDto(contentImageUrl = "")

        // When
        val result1 = recentlyViewedDto1.toEntity()
        val result2 = recentlyViewedDto2.toEntity()
        val result3 = recentlyViewedDto3.toEntity()

        // Then
        assertThat(result1.contentImageUrl).isEqualTo("/movie1.jpg")
        assertThat(result2.contentImageUrl).isEqualTo("/tv_show2.png")
        assertThat(result3.contentImageUrl).isEmpty()
    }

    @Test
    fun `RecentlyViewed toDto should map correctly with valid data`() {
        // Given
        val recentlyViewed = createMockRecentlyViewed(
            contentId = 123L,
            contentImageUrl = "/image123.jpg",
            contentType = RecentlyViewed.ContentType.MOVIE,
            viewedAt = Instant.fromEpochMilliseconds(1672574400000L)
                .toLocalDateTime(TimeZone.currentSystemDefault())
        )

        // When
        val result = recentlyViewed.toDto()

        // Then
        assertThat(result.contentId).isEqualTo(123L)
        assertThat(result.contentImageUrl).isEqualTo("/image123.jpg")
        assertThat(result.contentType).isEqualTo(RecentlyViewedDto.ContentType.MOVIE)
        assertThat(result.viewedAtEpochMillis).isEqualTo(1672574400000L)
    }

    @Test
    fun `RecentlyViewed toDto should handle TV_SHOW content type`() {
        // Given
        val recentlyViewed = createMockRecentlyViewed(
            contentId = 456L,
            contentImageUrl = "/image456.jpg",
            contentType = RecentlyViewed.ContentType.TV_SHOW,
            viewedAt = Instant.fromEpochMilliseconds(1672574400000L)
                .toLocalDateTime(TimeZone.currentSystemDefault())
        )

        // When
        val result = recentlyViewed.toDto()

        // Then
        assertThat(result.contentId).isEqualTo(456L)
        assertThat(result.contentImageUrl).isEqualTo("/image456.jpg")
        assertThat(result.contentType).isEqualTo(RecentlyViewedDto.ContentType.TV_SHOW)
    }

    @Test
    fun `RecentlyViewed toDto should handle different timestamps`() {
        // Given
        val timestamp1 = 1640995200000L // 2022-01-01 00:00:00 UTC
        val timestamp2 = 1704067200000L // 2024-01-01 00:00:00 UTC
        val recentlyViewed1 = createMockRecentlyViewed(
            contentId = 1L,
            viewedAt = Instant.fromEpochMilliseconds(timestamp1)
                .toLocalDateTime(TimeZone.currentSystemDefault())
        )
        val recentlyViewed2 = createMockRecentlyViewed(
            contentId = 2L,
            viewedAt = Instant.fromEpochMilliseconds(timestamp2)
                .toLocalDateTime(TimeZone.currentSystemDefault())
        )

        // When
        val result1 = recentlyViewed1.toDto()
        val result2 = recentlyViewed2.toDto()

        // Then
        assertThat(result1.viewedAtEpochMillis).isEqualTo(timestamp1)
        assertThat(result2.viewedAtEpochMillis).isEqualTo(timestamp2)
    }

    @Test
    fun `RecentlyViewed toDto should handle different content IDs`() {
        // Given
        val recentlyViewed1 = createMockRecentlyViewed(contentId = 1L)
        val recentlyViewed2 = createMockRecentlyViewed(contentId = 999999L)
        val recentlyViewed3 = createMockRecentlyViewed(contentId = 0L)

        // When
        val result1 = recentlyViewed1.toDto()
        val result2 = recentlyViewed2.toDto()
        val result3 = recentlyViewed3.toDto()

        // Then
        assertThat(result1.contentId).isEqualTo(1L)
        assertThat(result2.contentId).isEqualTo(999999L)
        assertThat(result3.contentId).isEqualTo(0L)
    }

    @Test
    fun `RecentlyViewed toDto should handle different image URLs`() {
        // Given
        val recentlyViewed1 = createMockRecentlyViewed(contentImageUrl = "/movie1.jpg")
        val recentlyViewed2 = createMockRecentlyViewed(contentImageUrl = "/tv_show2.png")
        val recentlyViewed3 = createMockRecentlyViewed(contentImageUrl = "")

        // When
        val result1 = recentlyViewed1.toDto()
        val result2 = recentlyViewed2.toDto()
        val result3 = recentlyViewed3.toDto()

        // Then
        assertThat(result1.contentImageUrl).isEqualTo("/movie1.jpg")
        assertThat(result2.contentImageUrl).isEqualTo("/tv_show2.png")
        assertThat(result3.contentImageUrl).isEmpty()
    }

    companion object {
        private fun createMockRecentlyViewedDto(
            contentId: Long = 123L,
            contentImageUrl: String = "/image123.jpg",
            contentType: RecentlyViewedDto.ContentType = RecentlyViewedDto.ContentType.MOVIE,
            viewedAtEpochMillis: Long = 1672574400000L
        ) = RecentlyViewedDto(
            contentId = contentId,
            contentImageUrl = contentImageUrl,
            contentType = contentType,
            viewedAtEpochMillis = viewedAtEpochMillis
        )

        private fun createMockRecentlyViewed(
            contentId: Long = 123L,
            contentImageUrl: String = "/image123.jpg",
            contentType: RecentlyViewed.ContentType = RecentlyViewed.ContentType.MOVIE,
            viewedAt: kotlinx.datetime.LocalDateTime = Instant.fromEpochMilliseconds(1672574400000L)
                .toLocalDateTime(TimeZone.currentSystemDefault())
        ) = RecentlyViewed(
            contentId = contentId,
            contentImageUrl = contentImageUrl,
            contentType = contentType,
            viewedAt = viewedAt
        )
    }
} 