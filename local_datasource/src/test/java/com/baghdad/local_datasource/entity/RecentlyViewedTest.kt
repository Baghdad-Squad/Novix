package com.baghdad.local_datasource.entity

import com.baghdad.local_datasource.roomDB.entity.RecentlyViewed
import com.baghdad.local_datasource.roomDB.entity.toDto
import com.baghdad.local_datasource.roomDB.entity.toLocalDto
import com.baghdad.repository.model.RecentlyViewedDto
import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.Test

class RecentlyViewedTest {

    @Test
    fun `should create RecentlyViewed and check fields`() {
        // Then
        assertThat(RECENTLY_VIEWED.contentId).isEqualTo(1L)
        assertThat(RECENTLY_VIEWED.contentType).isEqualTo("TV_SHOW")
        assertThat(RECENTLY_VIEWED.contentImageURL).isEqualTo("https://example.com/poster.jpg")
        assertThat(RECENTLY_VIEWED.viewedAt).isGreaterThan(0L)
    }

    @Test
    fun `should map RecentlyViewed to RecentlyViewedDto`() {
        // Given
        val recentlyViewedDto = RECENTLY_VIEWED.toDto()

        // Then
        assertThat(recentlyViewedDto.contentId).isEqualTo(RECENTLY_VIEWED.contentId)
        assertThat(recentlyViewedDto.contentType)
            .isEqualTo(RecentlyViewedDto.ContentType.valueOf(RECENTLY_VIEWED.contentType))
    }

    @Test
    fun `should map RecentlyViewedDto to RecentlyViewed`() {
        // Given
        val recentlyViewedDto = RECENTLY_VIEWED.toDto()

        // When
        val recentlyViewed = recentlyViewedDto.toLocalDto()

        // Then
        assertThat(recentlyViewed.contentId).isEqualTo(recentlyViewedDto.contentId)
        assertThat(recentlyViewed.contentType).isEqualTo(recentlyViewedDto.contentType.name)
        assertThat(recentlyViewed.contentImageURL).isEqualTo(recentlyViewedDto.contentImageUrl)
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