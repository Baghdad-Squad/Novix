package com.baghdad.localDatasource.mapper

import com.baghdad.localDatasource.roomDB.entity.RecentlyViewed
import com.baghdad.repository.model.RecentlyViewedDto
import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.Test

class RecentlyViewedMapperTest {

    @Test
    fun `should map RecentlyViewed entity to RecentlyViewedDto correctly`() {
        val result = testEntity.toDto()

        assertThat(result).isEqualTo(testDto)
    }

    @Test
    fun `should map RecentlyViewedDto to RecentlyViewed entity correctly`() {
        val result = testDto.toLocalDto()

        assertThat(result).isEqualTo(testEntity)
    }

    companion object {
        private val testEntity = RecentlyViewed(
            contentId = 1L,
            contentType = "MOVIE",
            contentImageURL = "http://example.com/image.png",
            viewedAt = 1640995200000L
        )

        private val testDto = RecentlyViewedDto(
            contentId = 1L,
            contentType = RecentlyViewedDto.ContentType.MOVIE,
            contentImageUrl = "http://example.com/image.png",
            viewedAtEpochMillis = 1640995200000L
        )
    }
}