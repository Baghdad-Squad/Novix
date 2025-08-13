package com.baghdad.remoteDataSource.mapper.castMembers

import com.baghdad.remoteDataSource.response.castMembers.CastMembersResponse
import com.baghdad.repository.model.ActorDto
import com.baghdad.repository.model.CastMemberDto
import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.Test

class CastMembersMapperTest {

    companion object {
        private val COMPLETE_CAST_RESPONSE = CastMembersResponse(
            id = 1L,
            cast = listOf(
                CastMembersResponse.CastMemberResponse(
                    id = 101L,
                    name = "Bryan Cranston",
                    profilePath = "/bryan.jpg",
                    knownForDepartment = "Acting",
                    character = "Walter White"
                ),
                CastMembersResponse.CastMemberResponse(
                    id = 202L,
                    name = "Aaron Paul",
                    profilePath = "/aaron.jpg",
                    knownForDepartment = "Acting",
                    character = "Jesse Pinkman"
                )
            )
        )

        private val NULL_CAST_RESPONSE = CastMembersResponse(
            id = 2L,
            cast = null
        )

        private val EMPTY_CAST_RESPONSE = CastMembersResponse(
            id = 3L,
            cast = emptyList()
        )

        private val NULL_VALUES_CAST_RESPONSE = CastMembersResponse(
            id = 4L,
            cast = listOf(
                CastMembersResponse.CastMemberResponse(
                    id = 303L,
                    name = null,
                    profilePath = null,
                    knownForDepartment = null,
                    character = null
                )
            )
        )

        private val NULL_ID_CAST_RESPONSE = CastMembersResponse(
            id = 5L,
            cast = listOf(
                CastMembersResponse.CastMemberResponse(
                    id = null,
                    name = "Invalid Actor",
                    profilePath = "/invalid.jpg",
                    knownForDepartment = "Acting",
                    character = "Unknown"
                ),
                CastMembersResponse.CastMemberResponse(
                    id = 404L,
                    name = "Valid Actor",
                    profilePath = "/valid.jpg",
                    knownForDepartment = "Directing",
                    character = "Director"
                )
            )
        )

        private val EXPECTED_COMPLETE_CAST = listOf(
            CastMemberDto(
                actor = ActorDto(
                    id = 101,
                    name = "Bryan Cranston",
                    imageUrl = "https://image.tmdb.org/t/p/w500/bryan.jpg",
                    biography = "",
                    birthdayDate = null,
                    deathDate = null,
                    placeOfBirth = "",
                    headerPictures = emptyList(),
                    department = "Acting"
                ),
                characterName = "Walter White"
            ),
            CastMemberDto(
                actor = ActorDto(
                    id = 202,
                    name = "Aaron Paul",
                    imageUrl = "https://image.tmdb.org/t/p/w500/aaron.jpg",
                    biography = "",
                    birthdayDate = null,
                    deathDate = null,
                    placeOfBirth = "",
                    headerPictures = emptyList(),
                    department = "Acting"
                ),
                characterName = "Jesse Pinkman"
            )
        )

        private val EXPECTED_EMPTY_LIST = emptyList<CastMemberDto>()

        private val EXPECTED_NULL_VALUES_CAST = listOf(
            CastMemberDto(
                actor = ActorDto(
                    id = 303,
                    name = "",
                    imageUrl = "",
                    biography = "",
                    birthdayDate = null,
                    deathDate = null,
                    placeOfBirth = "",
                    headerPictures = emptyList(),
                    department = ""
                ),
                characterName = ""
            )
        )

        private val EXPECTED_FILTERED_NULL_ID = listOf(
            CastMemberDto(
                actor = ActorDto(
                    id = 404,
                    name = "Valid Actor",
                    imageUrl = "https://image.tmdb.org/t/p/w500/valid.jpg",
                    biography = "",
                    birthdayDate = null,
                    deathDate = null,
                    placeOfBirth = "",
                    headerPictures = emptyList(),
                    department = "Directing"
                ),
                characterName = "Director"
            )
        )
    }

    @Test
    fun `should convert all valid cast members to CastMemberDto list`() {
        val castResponse = COMPLETE_CAST_RESPONSE

        val result = castResponse.toCastMembers()

        assertThat(result).isEqualTo(EXPECTED_COMPLETE_CAST)
    }

    @Test
    fun `should return empty list when cast is null`() {
        val castResponse = NULL_CAST_RESPONSE

        val result = castResponse.toCastMembers()

        assertThat(result).isEqualTo(EXPECTED_EMPTY_LIST)
    }

    @Test
    fun `should return empty list when cast is empty`() {
        val castResponse = EMPTY_CAST_RESPONSE

        val result = castResponse.toCastMembers()

        assertThat(result).isEqualTo(EXPECTED_EMPTY_LIST)
    }

    @Test
    fun `should handle null values by using default values`() {
        val castResponse = NULL_VALUES_CAST_RESPONSE

        val result = castResponse.toCastMembers()

        assertThat(result).isEqualTo(EXPECTED_NULL_VALUES_CAST)
    }

    @Test
    fun `should filter out cast members with null IDs`() {
        val castResponse = NULL_ID_CAST_RESPONSE

        val result = castResponse.toCastMembers()

        assertThat(result).isEqualTo(EXPECTED_FILTERED_NULL_ID)
    }
}