package com.baghdad.remoteDataSource.mapper.actor

import com.baghdad.remoteDataSource.response.actor.ActorDetailsResponse
import com.google.common.truth.Truth.assertThat
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
        assertThat(actor.id).isEqualTo(1)
        assertThat(actor.name).isEqualTo("Leonardo DiCaprio")
        assertThat(actor.biography).contains("American actor")
        assertThat(actor.birthday).isEqualTo("1974-11-11")
        assertThat(actor.deathday).isNull()
        assertThat(actor.homepage).isEqualTo("https://www.leonardodicaprio.com")
        assertThat(actor.knownForDepartment).isEqualTo("Acting")
        assertThat(actor.placeOfBirth).isEqualTo("Los Angeles, California, USA")
        assertThat(actor.profilePath).isEqualTo("/leo.jpg")
    }

    @Test
    fun `should create ActorDetailsResponse with default values when no fields are provided`() {
        // Given & When
        val actor = ActorDetailsResponse()

        // Then
        assertThat(actor.id).isNull()
        assertThat(actor.name).isNull()
        assertThat(actor.biography).isNull()
        assertThat(actor.birthday).isNull()
        assertThat(actor.deathday).isNull()
        assertThat(actor.homepage).isNull()
        assertThat(actor.knownForDepartment).isNull()
        assertThat(actor.placeOfBirth).isNull()
        assertThat(actor.profilePath).isNull()
    }

    @Test
    fun `should create ActorDetailsResponse with partial data when some fields are provided`() {
        // When
        val actor = ActorDetailsResponse(
            name = "Emma Watson",
            birthday = "1990-04-15"
        )

        // Then
        assertThat(actor.id).isNull()
        assertThat(actor.name).isEqualTo("Emma Watson")
        assertThat(actor.birthday).isEqualTo("1990-04-15")
        assertThat(actor.biography).isNull()
        assertThat(actor.profilePath).isNull()
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
        assertThat(actor.id).isNull()
        assertThat(actor.name).isNull()
        assertThat(actor.biography).isNull()
        assertThat(actor.birthday).isNull()
        assertThat(actor.deathday).isNull()
        assertThat(actor.homepage).isNull()
        assertThat(actor.knownForDepartment).isNull()
        assertThat(actor.placeOfBirth).isNull()
        assertThat(actor.profilePath).isNull()
    }
}