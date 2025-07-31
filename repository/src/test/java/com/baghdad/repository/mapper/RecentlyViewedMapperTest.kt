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
    fun `should map to entity correctly when dto contains valid movie data`() {
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
    fun `should map to entity correctly when dto contains TvShow content type`() {
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