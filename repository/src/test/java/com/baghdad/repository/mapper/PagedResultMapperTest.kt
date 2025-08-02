package com.baghdad.repository.mapper

import com.baghdad.repository.model.PagedResultDto
import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.Test

class PagedResultMapperTest {

    @Test
    fun `should map correctly when PagedResultDto has valid data`() {
        // Given
        val mockData = listOf("item1", "item2", "item3")
        val pagedResultDto = PagedResultDto(
            data = mockData,
            nextKey = 2,
            prevKey = null
        )

        // When
        val result = pagedResultDto.toPagedResult { it.uppercase() }

        // Then
        assertThat(result.data.size).isEqualTo(3)
        assertThat(result.data[0]).isEqualTo("ITEM1")
        assertThat(result.data[1]).isEqualTo("ITEM2")
        assertThat(result.data[2]).isEqualTo("ITEM3")
        assertThat(result.nextKey).isEqualTo(2)
        assertThat(result.prevKey).isNull()
    }

    @Test
    fun `should return empty result when PagedResultDto has empty data list`() {
        // Given
        val pagedResultDto = PagedResultDto(
            data = emptyList<String>(),
            nextKey = null,
            prevKey = null
        )

        // When
        val result = pagedResultDto.toPagedResult { it.uppercase() }

        // Then
        assertThat(result.data).isEmpty()
        assertThat(result.nextKey).isNull()
        assertThat(result.prevKey).isNull()
    }

} 