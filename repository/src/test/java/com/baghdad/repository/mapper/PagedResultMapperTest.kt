package com.baghdad.repository.mapper

import com.baghdad.repository.model.PagedResultDto
import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.Test

class PagedResultMapperTest {

    @Test
    fun `PagedResultDto toPagedResult should map correctly with valid data`() {
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
    fun `PagedResultDto toPagedResult should handle empty data list`() {
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

    @Test
    fun `PagedResultDto toPagedResult should handle single item`() {
        // Given
        val pagedResultDto = PagedResultDto(
            data = listOf("single"),
            nextKey = null,
            prevKey = null
        )

        // When
        val result = pagedResultDto.toPagedResult { it.uppercase() }

        // Then
        assertThat(result.data.size).isEqualTo(1)
        assertThat(result.data[0]).isEqualTo("SINGLE")
        assertThat(result.nextKey).isNull()
        assertThat(result.prevKey).isNull()
    }

    @Test
    fun `PagedResultDto toPagedResult should handle both next and prev keys`() {
        // Given
        val pagedResultDto = PagedResultDto(
            data = listOf("item1", "item2"),
            nextKey = 3,
            prevKey = 1
        )

        // When
        val result = pagedResultDto.toPagedResult { it.uppercase() }

        // Then
        assertThat(result.data.size).isEqualTo(2)
        assertThat(result.data[0]).isEqualTo("ITEM1")
        assertThat(result.data[1]).isEqualTo("ITEM2")
        assertThat(result.nextKey).isEqualTo(3)
        assertThat(result.prevKey).isEqualTo(1)
    }

    @Test
    fun `PagedResultDto toPagedResult should handle complex data transformation`() {
        // Given
        data class TestDto(val id: Int, val name: String)
        data class TestEntity(val id: Int, val name: String, val description: String)

        val testDtos = listOf(
            TestDto(1, "Item1"),
            TestDto(2, "Item2"),
            TestDto(3, "Item3")
        )
        val pagedResultDto = PagedResultDto(
            data = testDtos,
            nextKey = 2,
            prevKey = null
        )

        // When
        val result = pagedResultDto.toPagedResult { dto ->
            TestEntity(
                id = dto.id,
                name = dto.name,
                description = "Description for ${dto.name}"
            )
        }

        // Then
        assertThat(result.data.size).isEqualTo(3)
        assertThat(result.data[0].id).isEqualTo(1)
        assertThat(result.data[0].name).isEqualTo("Item1")
        assertThat(result.data[0].description).isEqualTo("Description for Item1")
        assertThat(result.data[1].id).isEqualTo(2)
        assertThat(result.data[1].name).isEqualTo("Item2")
        assertThat(result.data[1].description).isEqualTo("Description for Item2")
        assertThat(result.data[2].id).isEqualTo(3)
        assertThat(result.data[2].name).isEqualTo("Item3")
        assertThat(result.data[2].description).isEqualTo("Description for Item3")
        assertThat(result.nextKey).isEqualTo(2)
        assertThat(result.prevKey).isNull()
    }

    @Test
    fun `PagedResultDto toPagedResult should handle null keys`() {
        // Given
        val pagedResultDto = PagedResultDto(
            data = listOf("item1"),
            nextKey = null,
            prevKey = null
        )

        // When
        val result = pagedResultDto.toPagedResult { it.uppercase() }

        // Then
        assertThat(result.data.size).isEqualTo(1)
        assertThat(result.data[0]).isEqualTo("ITEM1")
        assertThat(result.nextKey).isNull()
        assertThat(result.prevKey).isNull()
    }

    @Test
    fun `PagedResultDto toPagedResult should handle identity transformation`() {
        // Given
        val pagedResultDto = PagedResultDto(
            data = listOf("item1", "item2"),
            nextKey = 2,
            prevKey = null
        )

        // When
        val result = pagedResultDto.toPagedResult { it }

        // Then
        assertThat(result.data.size).isEqualTo(2)
        assertThat(result.data[0]).isEqualTo("item1")
        assertThat(result.data[1]).isEqualTo("item2")
        assertThat(result.nextKey).isEqualTo(2)
        assertThat(result.prevKey).isNull()
    }

    @Test
    fun `PagedResultDto toPagedResult should handle numeric transformation`() {
        // Given
        val pagedResultDto = PagedResultDto(
            data = listOf("1", "2", "3", "4", "5"),
            nextKey = 3,
            prevKey = 1
        )

        // When
        val result = pagedResultDto.toPagedResult { it.toInt() }

        // Then
        assertThat(result.data.size).isEqualTo(5)
        assertThat(result.data[0]).isEqualTo(1)
        assertThat(result.data[1]).isEqualTo(2)
        assertThat(result.data[2]).isEqualTo(3)
        assertThat(result.data[3]).isEqualTo(4)
        assertThat(result.data[4]).isEqualTo(5)
        assertThat(result.nextKey).isEqualTo(3)
        assertThat(result.prevKey).isEqualTo(1)
    }
} 