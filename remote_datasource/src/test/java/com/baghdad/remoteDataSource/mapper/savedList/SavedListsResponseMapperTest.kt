package com.baghdad.remoteDataSource.mapper.savedList

import com.baghdad.remoteDataSource.mapper.toPagedSavedListsDtos
import com.baghdad.remoteDataSource.mapper.toSavedListDto
import com.baghdad.remoteDataSource.response.UserListDto
import com.baghdad.remoteDataSource.response.UserListsResponse
import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.Test

class SavedListsResponseMapperTest {

    @Test
    fun `toPagedSavedListsDtos should map UserListsResponse to PagedResultDto correctly`() {
        // Given
        val response = UserListsResponse(
            page = 2,
            totalPages = 3,
            results = listOf(
                UserListDto(id = 1L, name = "My Favorites", itemCount = 10),
                UserListDto(id = 2L, name = "Watch Later", itemCount = 3),
            )
        )
        // When
        val result = response.toPagedSavedListsDtos()

        // Then
        assertThat(result.data[0].id).isEqualTo(1L)
        assertThat(result.data[0].name).isEqualTo("My Favorites")
        assertThat(result.data[0].itemCount).isEqualTo(10)
        assertThat(result.data[1].id).isEqualTo(2L)
        assertThat(result.data[1].name).isEqualTo("Watch Later")
        assertThat(result.data[1].itemCount).isEqualTo(3)
        assertThat(result.nextKey).isEqualTo(3)
        assertThat(result.prevKey).isEqualTo(1)
    }

    @Test
    fun `toPagedSavedListsDtos should return empty list when results is null`() {
        // Given


        val response = UserListsResponse(
            page = 1,
            results = null,
            totalPages = 0,
            totalResults = 0
        )

        // When
        val result = response.toPagedSavedListsDtos()

        // Then
        assertThat(result.data).isEmpty()
        assertThat(result.nextKey).isNull()
        assertThat(result.prevKey).isNull()
    }

    @Test
    fun `toPagedSavedListsDtos should return empty list when results is empty`() {
        // Given
        val response = UserListsResponse(
            page = 1,
            results = emptyList(),
            totalPages = 0,
            totalResults = 0
        )

        // When
        val result = response.toPagedSavedListsDtos()

        // Then
        assertThat(result.data).isEmpty()
        assertThat(result.nextKey).isNull()
        assertThat(result.prevKey).isNull()
    }

    @Test
    fun `toPagedSavedListsDtos should handle first page correctly`() {
        // Given
        val userListDto = UserListDto(id = 1L, name = "Test List", itemCount = 5)
        val response = UserListsResponse(
            page = 1,
            results = listOf(userListDto),
            totalPages = 3,
            totalResults = 50
        )

        // When
        val result = response.toPagedSavedListsDtos()

        // Then
        assertThat(result.data).hasSize(1)
        assertThat(result.nextKey).isEqualTo(2)
        assertThat(result.prevKey).isNull()
    }

    @Test
    fun `toPagedSavedListsDtos should handle last page correctly`() {
        // Given
        val userListDto = UserListDto(id = 1L, name = "Test List", itemCount = 5)
        val response = UserListsResponse(
            page = 3,
            results = listOf(userListDto),
            totalPages = 3,
            totalResults = 50
        )

        // When
        val result = response.toPagedSavedListsDtos()

        // Then
        assertThat(result.data).hasSize(1)
        assertThat(result.nextKey).isNull()
        assertThat(result.prevKey).isEqualTo(2)
    }

    @Test
    fun `toSavedListDto should map UserListDto to SavedListDto correctly`() {
        // Given
        val userListDto = UserListDto(
            id = 123L,
            name = "Action Movies",
            itemCount = 25,
            description = "Best action movies",
            favoriteCount = 10
        )

        // When
        val result = userListDto.toSavedListDto()

        // Then
        assertThat(result.id).isEqualTo(123L)
        assertThat(result.name).isEqualTo("Action Movies")
        assertThat(result.itemCount).isEqualTo(25)
    }

    @Test
    fun `toSavedListDto should handle null values correctly`() {
        // Given
        val userListDto = UserListDto(
            id = null,
            name = null,
            itemCount = null,
            description = null,
            favoriteCount = null
        )

        // When
        val result = userListDto.toSavedListDto()

        // Then
        assertThat(result.id).isEqualTo(0L)
        assertThat(result.name).isEmpty()
        assertThat(result.itemCount).isEqualTo(0)
    }

    @Test
    fun `toSavedListDto should handle partial null values correctly`() {
        // Given
        val userListDto = UserListDto(
            id = 456L,
            name = null,
            itemCount = 15,
            description = "Some description",
            favoriteCount = null
        )

        // When
        val result = userListDto.toSavedListDto()

        // Then
        assertThat(result.id).isEqualTo(456L)
        assertThat(result.name).isEmpty()
        assertThat(result.itemCount).isEqualTo(15)
    }
}