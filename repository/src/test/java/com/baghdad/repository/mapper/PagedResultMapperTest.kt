package com.baghdad.repository.mapper

import com.baghdad.domain.model.PagedResult
import com.baghdad.repository.model.PagedResultDto
import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.Test

class PagedResultMapperTest {

    @Test
    fun `should return PagedResult when PagedResultDto has valid data`() {
        val mockData = listOf("item1", "item2", "item3")
        val pagedResultDto = PagedResultDto(
            data = mockData,
            nextKey = 2,
            prevKey = null
        )

        val result = pagedResultDto.toPagedResult { it.uppercase() }

        val expectedResult = PagedResult(
            data = listOf("ITEM1", "ITEM2", "ITEM3"),
            nextKey = 2,
            prevKey = null
        )

        assertThat(result).isEqualTo(expectedResult)
    }
} 