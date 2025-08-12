package com.baghdad.remoteDataSource.mapper.search

import com.baghdad.remoteDataSource.response.search.ActorSearchResponse
import com.baghdad.repository.model.ActorDto
import com.baghdad.repository.model.PagedResultDto
import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.Test

class ActorSearchResponseMapperTest {

    companion object {
        private val COMPLETE_RESPONSE = ActorSearchResponse(
            page = 1,
            totalPages = 5,
            results = listOf(
                ActorSearchResponse.Result(
                    id = 123L,
                    name = "Tom Hanks",
                    knownForDepartment = "Acting",
                    profilePath = "/hanks.jpg"
                ),
                ActorSearchResponse.Result(
                    id = 456L,
                    name = "Meryl Streep",
                    knownForDepartment = "Acting",
                    profilePath = "/streep.jpg"
                )
            )
        )

        private val EXPECTED_COMPLETE_DTO = PagedResultDto(
            data = listOf(
                ActorDto(
                    id = 123L,
                    name = "Tom Hanks",
                    imageUrl = "https://image.tmdb.org/t/p/w500/hanks.jpg",
                    biography = "",
                    birthdayDate = null,
                    deathDate = null,
                    placeOfBirth = "",
                    headerPictures = emptyList(),
                    department = "Acting"
                ),
                ActorDto(
                    id = 456L,
                    name = "Meryl Streep",
                    imageUrl = "https://image.tmdb.org/t/p/w500/streep.jpg",
                    biography = "",
                    birthdayDate = null,
                    deathDate = null,
                    placeOfBirth = "",
                    headerPictures = emptyList(),
                    department = "Acting"
                )
            ),
            nextKey = 2,
            prevKey = null
        )

        private val NULL_VALUES_RESPONSE = ActorSearchResponse(
            page = null,
            totalPages = null,
            results = listOf(
                ActorSearchResponse.Result(
                    id = null,
                    name = null,
                    knownForDepartment = null,
                    profilePath = null
                )
            )
        )

        private val MIXED_RESPONSE = ActorSearchResponse(
            page = 2,
            totalPages = 3,
            results = listOf(
                null,
                ActorSearchResponse.Result(
                    id = 789L,
                    name = "Leonardo DiCaprio",
                    knownForDepartment = "Acting",
                    profilePath = "/dicaprio.jpg"
                ),
                ActorSearchResponse.Result(
                    id = null,
                    name = "Invalid Actor",
                    profilePath = "/invalid.jpg"
                )
            )
        )
    }

    @Test
    fun `should convert complete ActorSearchResponse to PagedResultDto`() {
        val result = COMPLETE_RESPONSE.toPagedActorDtos()

        assertThat(result).isEqualTo(EXPECTED_COMPLETE_DTO)
    }

    @Test
    fun `should filter out results with null id`() {
        val result = NULL_VALUES_RESPONSE.toPagedActorDtos()

        assertThat(result.data).isEmpty()
    }

    @Test
    fun `should filter out null results and results with null id`() {
        val result = MIXED_RESPONSE.toPagedActorDtos()

        assertThat(result.data).hasSize(1)
    }

    @Test
    fun `should handle empty results list`() {
        val response = ActorSearchResponse(
            page = 1,
            totalPages = 1,
            results = emptyList()
        )
        val result = response.toPagedActorDtos()

        assertThat(result.data).isEmpty()
    }

    @Test
    fun `should handle null results`() {
        val response = ActorSearchResponse(
            page = 1,
            totalPages = 1,
            results = null
        )
        val result = response.toPagedActorDtos()

        assertThat(result.data).isEmpty()
    }

    @Test
    fun `should use empty string for null name and department`() {
        val response = ActorSearchResponse(
            results = listOf(
                ActorSearchResponse.Result(
                    id = 999L,
                    name = null,
                    knownForDepartment = null,
                    profilePath = null
                )
            )
        )
        val result = response.toPagedActorDtos()

        assertThat(result.data[0].name).isEmpty()
        assertThat(result.data[0].department).isEmpty()
    }

    @Test
    fun `should use empty string for imageUrl when profilePath is null`() {
        val response = ActorSearchResponse(
            results = listOf(
                ActorSearchResponse.Result(
                    id = 111L,
                    name = "Actor",
                    knownForDepartment = "Acting",
                    profilePath = null
                )
            )
        )
        val result = response.toPagedActorDtos()

        assertThat(result.data[0].imageUrl).isEmpty()
    }
}
