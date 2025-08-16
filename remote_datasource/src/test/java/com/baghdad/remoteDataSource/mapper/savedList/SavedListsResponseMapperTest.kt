package com.baghdad.remoteDataSource.mapper.savedList

import com.baghdad.remoteDataSource.response.savedList.UserListsResponse
import com.baghdad.repository.model.PagedResultDto
import com.baghdad.repository.model.SavedListDto
import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.Test

class SavedListsResponseMapperTest {

    companion object {
        private val COMPLETE_RESPONSE = UserListsResponse(
            page = 1,
            totalPages = 3,
            results = listOf(
                UserListsResponse.UserListDto(
                    id = 123L,
                    name = "Favorite Action Movies",
                    itemCount = 15,
                    description = "My favorite action films",
                    posterPath = "/action.jpg"
                ),
                UserListsResponse.UserListDto(
                    id = 456L,
                    name = "Sci-Fi Collection",
                    itemCount = 8,
                    description = "Best sci-fi movies",
                    posterPath = "/scifi.jpg"
                )
            )
        )

        private val EXPECTED_COMPLETE_DTO = PagedResultDto(
            data = listOf(
                SavedListDto(
                    id = 123L,
                    name = "Favorite Action Movies",
                    itemCount = 15
                ),
                SavedListDto(
                    id = 456L,
                    name = "Sci-Fi Collection",
                    itemCount = 8
                )
            ),
            nextKey = 2,
            prevKey = null
        )

        private val NULL_VALUES_RESPONSE = UserListsResponse(
            page = null,
            totalPages = null,
            results = listOf(
                UserListsResponse.UserListDto(
                    id = null,
                    name = null,
                    itemCount = null
                )
            )
        )

        private val EXPECTED_NULL_VALUES_DTO = PagedResultDto(
            data = listOf(
                SavedListDto(
                    id = -1L,
                    name = "",
                    itemCount = 0
                )
            ),
            nextKey = null,
            prevKey = null
        )

        private val EMPTY_RESULTS_RESPONSE = UserListsResponse(
            page = 2,
            totalPages = 3,
            results = emptyList()
        )

        private val NULL_RESULTS_RESPONSE = UserListsResponse(
            page = 1,
            totalPages = 1,
            results = null
        )
    }

    @Test
    fun `should convert complete UserListsResponse to PagedResultDto`() {
        val result = COMPLETE_RESPONSE.toPagedSavedListsDtos()

        assertThat(result).isEqualTo(EXPECTED_COMPLETE_DTO)
    }

    @Test
    fun `should handle null values in UserListDto`() {
        val result = NULL_VALUES_RESPONSE.toPagedSavedListsDtos()

        assertThat(result).isEqualTo(EXPECTED_NULL_VALUES_DTO)
    }

    @Test
    fun `should handle empty results list`() {
        val result = EMPTY_RESULTS_RESPONSE.toPagedSavedListsDtos()

        assertThat(result.data).isEmpty()
        assertThat(result.nextKey).isEqualTo(3)
        assertThat(result.prevKey).isEqualTo(1)
    }

    @Test
    fun `should handle null results`() {
        val result = NULL_RESULTS_RESPONSE.toPagedSavedListsDtos()

        assertThat(result.data).isEmpty()
        assertThat(result.nextKey).isNull()
        assertThat(result.prevKey).isNull()
    }
}