package com.baghdad.local_datasource.entity

import com.baghdad.local_datasource.roomDB.entity.RecentlyViewed
import com.baghdad.local_datasource.roomDB.entity.toDto
import com.baghdad.local_datasource.roomDB.entity.toLocalDto
import com.baghdad.repository.model.RecentlyViewedDto
import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.Test

class RecentlyViewedTest {

    @Test
    fun `should create RecentlyViewed with correct field values when using predefined instance`() {
        assertThat(RECENTLY_VIEWED.contentId).isEqualTo(1L)
        assertThat(RECENTLY_VIEWED.contentType).isEqualTo("TV_SHOW")
        assertThat(RECENTLY_VIEWED.contentImageURL).isEqualTo("https://example.com/poster.jpg")
        assertThat(RECENTLY_VIEWED.viewedAt).isEqualTo(123456789L)
    }

    @Test
    fun `should map to RecentlyViewedDto when calling toDto`() {
        // When
        val dto = RECENTLY_VIEWED.toDto()

        // Then
        assertThat(dto.contentId).isEqualTo(RECENTLY_VIEWED.contentId)
        assertThat(dto.contentType).isEqualTo(RecentlyViewedDto.ContentType.TV_SHOW)
        assertThat(dto.contentImageUrl).isEqualTo(RECENTLY_VIEWED.contentImageURL)
        assertThat(dto.viewedAtEpochMillis).isEqualTo(RECENTLY_VIEWED.viewedAt)
    }

    @Test
    fun `should map back to RecentlyViewed when converting from RecentlyViewedDto`() {
        // When
        val entity = RECENTLY_VIEWED.toDto().toLocalDto()

        // Then
        assertThat(entity).isEqualTo(RECENTLY_VIEWED)
    }

    @Test
    fun `should throw IllegalArgumentException when contentType is invalid`() {
        // Given
        val invalidEntity = RECENTLY_VIEWED.copy(contentType = "INVALID_TYPE")

        // When
        val result = runCatching { invalidEntity.toDto() }

        // Then
        assertThat(result.exceptionOrNull())
            .isInstanceOf(IllegalArgumentException::class.java)
    }

    @Test
    fun `should create RecentlyViewed with default viewedAt when not provided`() {
        // When
        val entity = RecentlyViewed(
            contentId = 10L,
            contentType = "MOVIE",
            contentImageURL = "poster.jpg"
        )

        // Then
        assertThat(entity.viewedAt).isGreaterThan(0L)
    }

    companion object {
        val RECENTLY_VIEWED = RecentlyViewed(
            contentId = 1L,
            contentType = "TV_SHOW",
            contentImageURL = "https://example.com/poster.jpg",
            viewedAt = 123456789L
        )
    }
}
