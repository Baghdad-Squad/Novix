package com.baghdad.remoteDataSource.mapper.actor

import com.baghdad.remoteDataSource.response.actor.ActorDetailsResponse
import com.baghdad.repository.model.ActorDto
import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.Test

class ActorDetailsMapperTest {
    companion object {
        private val COMPLETE_ACTOR_RESPONSE = ActorDetailsResponse(
            id = 123L,
            name = "John Doe",
            profilePath = "/actor_profile.jpg",
            biography = "A talented actor known for various roles.",
            birthday = "1980-01-15",
            deathday = null,
            placeOfBirth = "New York, USA",
            knownForDepartment = "Acting"
        )

        private val EXPECTED_COMPLETE_DTO = ActorDto(
            id = 123L,
            name = "John Doe",
            imageUrl = "https://image.tmdb.org/t/p/w500/actor_profile.jpg",
            biography = "A talented actor known for various roles.",
            birthdayDate = "1980-01-15",
            deathDate = null,
            placeOfBirth = "New York, USA",
            headerPictures = listOf("https://image.tmdb.org/t/p/w500/actor_profile.jpg"),
            department = "Acting"
        )

        private val NULL_VALUES_ACTOR_RESPONSE = ActorDetailsResponse(
            id = null,
            name = null,
            profilePath = null,
            biography = null,
            birthday = null,
            deathday = null,
            placeOfBirth = null,
            knownForDepartment = null
        )

        private val EXPECTED_NULL_VALUES_DTO = ActorDto(
            id = -1L,
            name = "",
            imageUrl = "",
            biography = "",
            birthdayDate = null,
            deathDate = null,
            placeOfBirth = "",
            headerPictures = listOf(""),
            department = ""
        )

        private val MIXED_NULL_ACTOR_RESPONSE = ActorDetailsResponse(
            id = 999L,
            name = null,
            profilePath = "/profile.jpg",
            biography = null,
            birthday = "1990-01-01",
            deathday = null,
            placeOfBirth = null,
            knownForDepartment = null
        )

        private val EXPECTED_MIXED_NULL_DTO = ActorDto(
            id = 999L,
            name = "",
            imageUrl = "https://image.tmdb.org/t/p/w500/profile.jpg",
            biography = "",
            birthdayDate = "1990-01-01",
            deathDate = null,
            placeOfBirth = "",
            headerPictures = listOf("https://image.tmdb.org/t/p/w500/profile.jpg"),
            department = ""
        )
    }

    @Test
    fun `should convert complete ActorDetailsResponse to ActorDto`() {
        val actorResponse = COMPLETE_ACTOR_RESPONSE

        val result = actorResponse.toDto()

        assertThat(result).isEqualTo(EXPECTED_COMPLETE_DTO)
    }

    @Test
    fun `should handle all null values by using default values`() {
        val actorResponse = NULL_VALUES_ACTOR_RESPONSE

        val result = actorResponse.toDto()

        assertThat(result).isEqualTo(EXPECTED_NULL_VALUES_DTO)
    }

    @Test
    fun `should handle mixed null and non-null values correctly`() {
        val actorResponse = MIXED_NULL_ACTOR_RESPONSE

        val result = actorResponse.toDto()

        assertThat(result).isEqualTo(EXPECTED_MIXED_NULL_DTO)
    }
}