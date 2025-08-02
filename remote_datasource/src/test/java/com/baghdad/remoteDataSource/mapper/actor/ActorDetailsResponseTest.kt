package com.baghdad.remoteDataSource.mapper.actor

import com.baghdad.remoteDataSource.response.actor.ActorDetailsResponse
import com.google.common.truth.Truth
import org.junit.jupiter.api.Test

class ActorDetailsResponseTest {

    @Test
    fun `should create ActorDetailsResponse with full data when all fields are provided`() {
        // Given
        val actor = ActorDetailsResponse(
            id = 1,
            name = "Leonardo DiCaprio",
            biography = "An American actor and film producer.",
            birthday = "1974-11-11",
            deathday = null,
            homepage = "https://www.leonardodicaprio.com",
            knownForDepartment = "Acting",
            placeOfBirth = "Los Angeles, California, USA",
            profilePath = "/leo.jpg"
        )

        // Then
        Truth.assertThat(actor.id).isEqualTo(1)
        Truth.assertThat(actor.name).isEqualTo("Leonardo DiCaprio")
        Truth.assertThat(actor.biography).contains("American actor")
        Truth.assertThat(actor.birthday).isEqualTo("1974-11-11")
        Truth.assertThat(actor.deathday).isNull()
        Truth.assertThat(actor.homepage).isEqualTo("https://www.leonardodicaprio.com")
        Truth.assertThat(actor.knownForDepartment).isEqualTo("Acting")
        Truth.assertThat(actor.placeOfBirth).isEqualTo("Los Angeles, California, USA")
        Truth.assertThat(actor.profilePath).isEqualTo("/leo.jpg")
    }

    @Test
    fun `should create ActorDetailsResponse with default values when no fields are provided`() {
        // Given & When
        val actor = ActorDetailsResponse()

        // Then
        Truth.assertThat(actor.id).isNull()
        Truth.assertThat(actor.name).isNull()
        Truth.assertThat(actor.biography).isNull()
        Truth.assertThat(actor.birthday).isNull()
        Truth.assertThat(actor.deathday).isNull()
        Truth.assertThat(actor.homepage).isNull()
        Truth.assertThat(actor.knownForDepartment).isNull()
        Truth.assertThat(actor.placeOfBirth).isNull()
        Truth.assertThat(actor.profilePath).isNull()
    }

    @Test
    fun `should create ActorDetailsResponse with partial data when some fields are provided`() {
        // When
        val actor = ActorDetailsResponse(
            name = "Emma Watson",
            birthday = "1990-04-15"
        )

        // Then
        Truth.assertThat(actor.id).isNull()
        Truth.assertThat(actor.name).isEqualTo("Emma Watson")
        Truth.assertThat(actor.birthday).isEqualTo("1990-04-15")
        Truth.assertThat(actor.biography).isNull()
        Truth.assertThat(actor.profilePath).isNull()
    }

    @Test
    fun `should allow null values for all fields`() {
        // Given & When
        val actor = ActorDetailsResponse(
            id = null,
            name = null,
            biography = null,
            birthday = null,
            deathday = null,
            homepage = null,
            knownForDepartment = null,
            placeOfBirth = null,
            profilePath = null
        )

        // Then
        Truth.assertThat(actor.id).isNull()
        Truth.assertThat(actor.name).isNull()
        Truth.assertThat(actor.biography).isNull()
        Truth.assertThat(actor.birthday).isNull()
        Truth.assertThat(actor.deathday).isNull()
        Truth.assertThat(actor.homepage).isNull()
        Truth.assertThat(actor.knownForDepartment).isNull()
        Truth.assertThat(actor.placeOfBirth).isNull()
        Truth.assertThat(actor.profilePath).isNull()
    }
}