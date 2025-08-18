package com.baghdad.remoteDataSource.mapper.savedList

import com.baghdad.remoteDataSource.response.savedList.ListDetailsResponse
import com.baghdad.repository.model.MovieDto
import com.baghdad.repository.model.PagedResultDto
import com.baghdad.repository.model.SavedListDto
import com.baghdad.repository.model.savedList.SavableMovieDto
import com.baghdad.repository.model.savedList.SavedListDetailsDto
import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.Test

class ListDetailsResponseMapperTest {
    companion object {
        private val COMPLETE_RESPONSE = ListDetailsResponse(
            id = 123L,
            name = "My Favorite Movies",
            itemCount = 5,
            page = 1,
            totalPages = 2,
            items = listOf(
                ListDetailsResponse.Item(
                    id = 101L,
                    title = "Inception",
                    originalTitle = "Inception Original",
                    voteAverage = 8.4,
                    releaseDate = "2010-07-16",
                    overview = "A thief who steals corporate secrets...",
                    posterPath = "/inception.jpg"
                ),
                ListDetailsResponse.Item(
                    id = 102L,
                    title = null,
                    originalTitle = "The Dark Knight",
                    voteAverage = 9.0,
                    releaseDate = "2008-07-18",
                    overview = "When the menace known as the Joker...",
                    posterPath = "/dark_knight.jpg"
                )
            )
        )

        private val EXPECTED_COMPLETE_DTO = SavedListDetailsDto(
            savedList = SavedListDto(
                id = 123L,
                name = "My Favorite Movies",
                itemCount = 5
            ),
            pagedItems = PagedResultDto(
                data = listOf(
                    SavableMovieDto(
                        movie = MovieDto(
                            id = 101L,
                            title = "Inception",
                            genres = emptyList(),
                            imdbRating = 8.4,
                            userRating = null,
                            releaseDate = "2010-07-16",
                            overview = "A thief who steals corporate secrets...",
                            posterPictureURL = "https://image.tmdb.org/t/p/w500/inception.jpg",
                            trailerURL = "",
                            runtimeMinutes = 0
                        ),
                        isSaved = false,
                        listId = null
                    ),
                    SavableMovieDto(
                        movie = MovieDto(
                            id = 102L,
                            title = "The Dark Knight",
                            genres = emptyList(),
                            imdbRating = 9.0,
                            userRating = null,
                            releaseDate = "2008-07-18",
                            overview = "When the menace known as the Joker...",
                            posterPictureURL = "https://image.tmdb.org/t/p/w500/dark_knight.jpg",
                            trailerURL = "",
                            runtimeMinutes = 0
                        ),
                        isSaved = false,
                        listId = null
                    )
                ),
                nextKey = 2,
                prevKey = null
            )
        )

        private val NULL_VALUES_RESPONSE = ListDetailsResponse(
            id = null,
            name = null,
            itemCount = null,
            page = null,
            totalPages = null,
            items = null
        )

        private val EXPECTED_NULL_VALUES_DTO = SavedListDetailsDto(
            savedList = SavedListDto(
                id = -1L,
                name = "",
                itemCount = 0
            ),
            pagedItems = PagedResultDto(
                data = emptyList(),
                nextKey = null,
                prevKey = null
            )
        )

        private val EMPTY_ITEMS_RESPONSE = ListDetailsResponse(
            id = 456L,
            name = "Empty List",
            itemCount = 0,
            page = 1,
            totalPages = 1,
            items = emptyList()
        )

        private val ITEMS_WITH_NULL_VALUES = ListDetailsResponse(
            id = 789L,
            name = "List With Null Items",
            itemCount = 3,
            page = 1,
            totalPages = 1,
            items = listOf(
                ListDetailsResponse.Item(
                    id = null,
                    title = null,
                    originalTitle = null
                ),
                null,
                ListDetailsResponse.Item(
                    id = 103L,
                    title = "Interstellar",
                    originalTitle = null,
                    voteAverage = 8.6,
                    releaseDate = "2014-11-07",
                    overview = "A team of explorers travel through a wormhole...",
                    posterPath = "/interstellar.jpg"
                )
            )
        )
    }

    @Test
    fun `should convert complete ListDetailsResponse to SavedListDetailsDto`() {
        val result = COMPLETE_RESPONSE.toSavedListDetailsDto()

        assertThat(result).isEqualTo(EXPECTED_COMPLETE_DTO)
    }

    @Test
    fun `should handle null values in ListDetailsResponse`() {
        val result = NULL_VALUES_RESPONSE.toSavedListDetailsDto()

        assertThat(result).isEqualTo(EXPECTED_NULL_VALUES_DTO)
    }

    @Test
    fun `should handle empty items list`() {
        val result = EMPTY_ITEMS_RESPONSE.toSavedListDetailsDto()

        assertThat(result.pagedItems.data).isEmpty()
        assertThat(result.savedList.itemCount).isEqualTo(0)
    }

    @Test
    fun `should filter out null items and items with no title`() {
        val result = ITEMS_WITH_NULL_VALUES.toSavedListDetailsDto()

        assertThat(result.pagedItems.data).hasSize(1)
        assertThat(result.pagedItems.data[0].movie.title).isEqualTo("Interstellar")
    }

    @Test
    fun `should use originalTitle when title is null`() {
        val response = ListDetailsResponse(
            items = listOf(
                ListDetailsResponse.Item(
                    id = 104L,
                    title = null,
                    originalTitle = "Pulp Fiction"
                )
            )
        )
        val result = response.toSavedListDetailsDto()

        assertThat(result.pagedItems.data[0].movie.title).isEqualTo("Pulp Fiction")
    }

    @Test
    fun `should exclude items with both title and originalTitle null`() {
        val response = ListDetailsResponse(
            items = listOf(
                ListDetailsResponse.Item(
                    id = 105L,
                    title = null,
                    originalTitle = null
                )
            )
        )
        val result = response.toSavedListDetailsDto()

        assertThat(result.pagedItems.data).isEmpty()
    }
}
