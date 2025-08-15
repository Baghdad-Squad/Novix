package com.baghdad.repository.mapper

import com.baghdad.repository.dummyData.DummyDataFactory.DummyDataFactory.RECENTLY_VIEWED
import com.baghdad.repository.dummyData.DummyDataFactory.DummyDataFactory.RECENTLY_VIEWED_DTO
import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.Test

class RecentlyViewedMapperTest {
    @Test
    fun `should map RecentlyViewedDto contentId to entity contentId correctly`() {
        val result = RECENTLY_VIEWED

        assertThat(result.contentId).isEqualTo(RECENTLY_VIEWED_DTO.contentId)
    }

    @Test
    fun `should map RecentlyViewedDto contentImageUrl to entity contentImageUrl correctly`() {
        val result = RECENTLY_VIEWED

        assertThat(result.contentImageUrl).isEqualTo(RECENTLY_VIEWED_DTO.contentImageUrl)
    }

    @Test
    fun `should map RecentlyViewedDto contentType to entity contentType correctly`() {
        val result = RECENTLY_VIEWED

        assertThat(result.contentType.name).isEqualTo(RECENTLY_VIEWED_DTO.contentType.name)
    }

    @Test
    fun `should map isSaved parameter to entity isSaved correctly`() {
        val result = RECENTLY_VIEWED

        assertThat(result.isSaved).isTrue()
    }

    @Test
    fun `should map listId parameter to entity listId correctly`() {
        val result = RECENTLY_VIEWED

        assertThat(result.listId).isEqualTo(55L)
    }


}