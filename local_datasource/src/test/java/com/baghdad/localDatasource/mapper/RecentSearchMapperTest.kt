package com.baghdad.localDatasource.mapper

import com.baghdad.localDatasource.roomDB.entity.RecentSearch
import com.baghdad.repository.model.RecentSearchDto
import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.Test

class RecentSearchMapperTest {

    @Test
    fun `should map RecentSearch entity to RecentSearchDto correctly`() {
        val result = testEntity.toDto()

        assertThat(result).isEqualTo(testDto)
    }

    companion object {
        private val testEntity = RecentSearch(
            id = 1L,
            query = "action movies",
            searchedAt = 1640995200000L
        )

        private val testDto = RecentSearchDto(
            id = 1L,
            query = "action movies",
            searchedAt = 1640995200000L
        )
    }
}