package com.baghdad.local_datasource.entity

import com.baghdad.local_datasource.roomDB.entity.RecentlyViewed
import com.baghdad.local_datasource.roomDB.entity.toDto
import com.baghdad.local_datasource.roomDB.entity.toLocalDto
import com.baghdad.repository.model.RecentlyViewedDto
import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class RecentlyViewedTest {

    @Test
    fun `should create RecentlyViewed with correct values when instantiated`() {
        // Given
        val recentlyViewed = RECENTLY_VIEWED

        // Then
        assertThat(recentlyViewed.contentId).isEqualTo(1L)
        assertThat(recentlyViewed.contentType).isEqualTo("TV_SHOW")
        assertThat(recentlyViewed.contentImageURL).isEqualTo("https://example.com/poster.jpg")
        assertThat(recentlyViewed.viewedAt).isEqualTo(123456789L)
    }

    @Test
    fun `should map to RecentlyViewedDto when toDto is called`() {
        // Given
        val entity = RECENTLY_VIEWED

        // When
        val dto = entity.toDto()

        // Then
        assertThat(dto.contentId).isEqualTo(entity.contentId)
        assertThat(dto.contentType).isEqualTo(RecentlyViewedDto.ContentType.TV_SHOW)
        assertThat(dto.contentImageUrl).isEqualTo(entity.contentImageURL)
        assertThat(dto.viewedAtEpochMillis).isEqualTo(entity.viewedAt)
    }

    @Test
    fun `should map to RecentlyViewed when toLocalDto is called`() {
        // Given
        val dto = RECENTLY_VIEWED.toDto()

        // When
        val entity = dto.toLocalDto()

        // Then
        assertThat(entity).isEqualTo(RECENTLY_VIEWED)
    }

    @Test
    fun `should throw IllegalArgumentException when contentType is invalid`() {
        // Given
        val invalidEntity = RECENTLY_VIEWED.copy(contentType = "INVALID_TYPE")

        // When / Then
        assertThrows<IllegalArgumentException> {
            invalidEntity.toDto()
        }
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
