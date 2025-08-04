package com.baghdad.remoteDataSource.mapper.savedList

import com.baghdad.remoteDataSource.response.savedList.ListDetailsResponse
import com.baghdad.repository.model.savedList.SavedListItemDto
import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.Test

class ListDetailsResponseMapperTest {
    @Test
    fun `toSavedListDetailsDto should map data correctly when response is valid`() {
        // Given
        val response = VALID_LIST_DETAILS_RESPONSE

        // When
        val dto = response.toSavedListDetailsDto()

        // Then
        assertThat(dto.savedList.id).isEqualTo(123L)
        assertThat(dto.savedList.name).isEqualTo("My List")
        assertThat(dto.savedList.itemCount).isEqualTo(2)
        assertThat(dto.pagedItems.data).hasSize(2)

        val item1 = dto.pagedItems.data[0]
        assertThat(item1.id).isEqualTo(1L)
        assertThat(item1.type).isEqualTo(SavedListItemDto.Type.MOVIE)
        assertThat(item1.title).isEqualTo("Oppenheimer")
        assertThat(item1.posterUrl).isEqualTo("/path1.jpg")

        val item2 = dto.pagedItems.data[1]
        assertThat(item2.id).isEqualTo(2L)
        assertThat(item2.type).isEqualTo(SavedListItemDto.Type.TV_SHOW)
        assertThat(item2.title).isEqualTo("Breaking Bad")
        assertThat(item2.posterUrl).isEqualTo("/path2.jpg")
    }

    @Test
    fun `toSavedListDetailsDto should return empty listItems when items list is null`() {
        // Given
        val response = EMPTY_LIST_DETAILS_RESPONSE

        // When
        val dto = response.toSavedListDetailsDto()

        // Then
        assertThat(dto.savedList.id).isEqualTo(321L)
        assertThat(dto.savedList.name).isEqualTo("Empty Items List")
        assertThat(dto.savedList.itemCount).isEqualTo(0)
        assertThat(dto.pagedItems.data).isEmpty()
    }

    @Test
    fun `toSavedListDetailsDto should filter null data and skips invalid items`() {
        // Given
        val response = INVALID_LIST_DETAILS_RESPONSE

        // When
        val dto = response.toSavedListDetailsDto()

        // Then
        assertThat(dto.savedList.id).isEqualTo(-1L)
        assertThat(dto.savedList.name).isEmpty()
        assertThat(dto.savedList.itemCount).isEqualTo(0)
        assertThat(dto.pagedItems.data).isEmpty()
    }

    companion object {
        private val VALID_MOVIE_ITEM =
            ListDetailsResponse.Item(
                id = 1L,
                mediaType = "movie",
                title = "Oppenheimer",
                originalTitle = null,
                posterPath = "/path1.jpg",
            )

        private val VALID_TV_ITEM =
            ListDetailsResponse.Item(
                id = 2L,
                mediaType = "tv",
                title = null,
                originalTitle = "Breaking Bad",
                posterPath = "/path2.jpg",
            )

        private val VALID_LIST_DETAILS_RESPONSE =
            ListDetailsResponse(
                id = 123L,
                name = "My List",
                itemCount = 2,
                items =
                    listOf(
                        VALID_MOVIE_ITEM,
                        VALID_TV_ITEM,
                    ),
            )

        val INVALID_LIST_DETAILS_RESPONSE =
            ListDetailsResponse(
                id = null,
                name = null,
                itemCount = null,
                items =
                    listOf(
                        null,
                        ListDetailsResponse.Item(
                            id = null,
                            mediaType = "movie",
                            title = "Some Title",
                            originalTitle = null,
                            posterPath = null,
                        ),
                        ListDetailsResponse.Item(
                            id = 3L,
                            mediaType = "invalid",
                            title = "Title",
                            originalTitle = null,
                            posterPath = null,
                        ),
                        ListDetailsResponse.Item(
                            id = 4L,
                            mediaType = "tv",
                            title = null,
                            originalTitle = null,
                            posterPath = null,
                        ),
                    ),
            )
        private val EMPTY_LIST_DETAILS_RESPONSE =
            ListDetailsResponse(
                id = 321L,
                name = "Empty Items List",
                itemCount = 0,
                items = null,
            )
    }
}
