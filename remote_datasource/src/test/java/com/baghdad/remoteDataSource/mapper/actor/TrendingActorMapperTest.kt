package com.baghdad.remoteDataSource.mapper.actor

import com.baghdad.remoteDataSource.response.actor.TrendingActorResponse
import com.baghdad.repository.model.ActorDto
import com.baghdad.repository.model.PagedResultDto
import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.Test

class TrendingActorMapperTest {

    companion object {
        private val COMPLETE_ACTOR_RESPONSE = TrendingActorResponse(
            page = 1,
            totalPages = 3,
            results = listOf(
                TrendingActorResponse.TrendingActorDetails(
                    id = 101L,
                    name = "Bryan Cranston",
                    profilePath = "/bryan.jpg"
                ),
                TrendingActorResponse.TrendingActorDetails(
                    id = 202L,
                    name = "Millie Bobby Brown",
                    profilePath = "/millie.jpg"
                )
            )
        )

        private val NULL_RESULTS_RESPONSE = TrendingActorResponse(
            page = 1,
            totalPages = 3,
            results = null
        )

        private val EMPTY_RESULTS_RESPONSE = TrendingActorResponse(
            page = 1,
            totalPages = 3,
            results = emptyList()
        )

        private val NULL_VALUES_RESPONSE = TrendingActorResponse(
            page = 2,
            totalPages = 4,
            results = listOf(
                TrendingActorResponse.TrendingActorDetails(
                    id = 303L,
                    name = null,
                    profilePath = null
                )
            )
        )

        private val NULL_ID_RESPONSE = TrendingActorResponse(
            page = 1,
            totalPages = 2,
            results = listOf(
                TrendingActorResponse.TrendingActorDetails(
                    id = null,
                    name = "Invalid Actor",
                    profilePath = "/invalid.jpg"
                ),
                TrendingActorResponse.TrendingActorDetails(
                    id = 404L,
                    name = "Valid Actor",
                    profilePath = "/valid.jpg"
                )
            )
        )

        private val EXPECTED_COMPLETE_ACTORS = PagedResultDto(
            data = listOf(
                ActorDto(
                    id = 101,
                    name = "Bryan Cranston",
                    imageUrl = "https://image.tmdb.org/t/p/w500/bryan.jpg",
                    biography = "",
                    birthdayDate = null,
                    deathDate = null,
                    placeOfBirth = "",
                    headerPictures = emptyList(),
                    department = ""
                ),
                ActorDto(
                    id = 202,
                    name = "Millie Bobby Brown",
                    imageUrl = "https://image.tmdb.org/t/p/w500/millie.jpg",
                    biography = "",
                    birthdayDate = null,
                    deathDate = null,
                    placeOfBirth = "",
                    headerPictures = emptyList(),
                    department = ""
                )
            ),
            nextKey = 2,
            prevKey = null
        )

        private val EXPECTED_EMPTY_LIST = PagedResultDto<ActorDto>(
            data = emptyList(),
            nextKey = 2,
            prevKey = null
        )

        private val EXPECTED_NULL_VALUES = PagedResultDto(
            data = listOf(
                ActorDto(
                    id = 303,
                    name = "",
                    imageUrl = "",
                    biography = "",
                    birthdayDate = null,
                    deathDate = null,
                    placeOfBirth = "",
                    headerPictures = emptyList(),
                    department = ""
                )
            ),
            nextKey = 3,
            prevKey = 1
        )

        private val EXPECTED_FILTERED_NULL_ID = PagedResultDto(
            data = listOf(
                ActorDto(
                    id = 404,
                    name = "Valid Actor",
                    imageUrl = "https://image.tmdb.org/t/p/w500/valid.jpg",
                    biography = "",
                    birthdayDate = null,
                    deathDate = null,
                    placeOfBirth = "",
                    headerPictures = emptyList(),
                    department = ""
                )
            ),
            nextKey = 2,
            prevKey = null
        )
    }

    @Test
    fun `should convert all valid actors to ActorDto list`() {
        val actorResponse = COMPLETE_ACTOR_RESPONSE

        val result = actorResponse.toPagedActorDtos()

        assertThat(result).isEqualTo(EXPECTED_COMPLETE_ACTORS)
    }

    @Test
    fun `should return empty list when results is null`() {
        val actorResponse = NULL_RESULTS_RESPONSE

        val result = actorResponse.toPagedActorDtos()

        assertThat(result).isEqualTo(EXPECTED_EMPTY_LIST)
    }

    @Test
    fun `should return empty list when results is empty`() {
        val actorResponse = EMPTY_RESULTS_RESPONSE

        val result = actorResponse.toPagedActorDtos()

        assertThat(result).isEqualTo(EXPECTED_EMPTY_LIST)
    }

    @Test
    fun `should handle null values by using default values`() {
        val actorResponse = NULL_VALUES_RESPONSE

        val result = actorResponse.toPagedActorDtos()

        assertThat(result).isEqualTo(EXPECTED_NULL_VALUES)
    }

    @Test
    fun `should filter out actors with null IDs`() {
        val actorResponse = NULL_ID_RESPONSE

        val result = actorResponse.toPagedActorDtos()

        assertThat(result).isEqualTo(EXPECTED_FILTERED_NULL_ID)
    }
}
