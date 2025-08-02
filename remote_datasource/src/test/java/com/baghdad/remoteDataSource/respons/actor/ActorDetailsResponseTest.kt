package com.baghdad.remoteDataSource.respons.actor

import com.baghdad.remoteDataSource.response.actor.ActorDetailsResponse
import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.Test

class ActorDetailsResponseTest {

    @Test
    fun `should create ActorDetailsResponse with full data when all fields are provided`() {
        // Given
        val expectedId = 101
        val expectedName = "Leonardo DiCaprio"
        val expectedBiography = "An American actor and producer."
        val expectedBirthday = "1974-11-11"
        val expectedDeathday = null
        val expectedHomepage = "https://leonardo.com"
        val expectedKnownFor = "Acting"
        val expectedPlaceOfBirth = "Los Angeles, USA"
        val expectedProfilePath = "/profile/path.jpg"

        // When
        val actorDetails = ActorDetailsResponse(
            id = 101,
            name = "Leonardo DiCaprio",
            biography = "An American actor and producer.",
            birthday = "1974-11-11",
            deathday = null,
            homepage = "https://leonardo.com",
            knownForDepartment = "Acting",
            placeOfBirth = "Los Angeles, USA",
            profilePath = "/profile/path.jpg"
        )

        // Then
        assertThat(actorDetails.id).isEqualTo(expectedId)
        assertThat(actorDetails.name).isEqualTo(expectedName)
        assertThat(actorDetails.biography).isEqualTo(expectedBiography)
        assertThat(actorDetails.birthday).isEqualTo(expectedBirthday)
        assertThat(actorDetails.deathday).isEqualTo(expectedDeathday)
        assertThat(actorDetails.homepage).isEqualTo(expectedHomepage)
        assertThat(actorDetails.knownForDepartment).isEqualTo(expectedKnownFor)
        assertThat(actorDetails.placeOfBirth).isEqualTo(expectedPlaceOfBirth)
        assertThat(actorDetails.profilePath).isEqualTo(expectedProfilePath)
    }

    @Test
    fun `should create ActorDetailsResponse with default values when no fields are provided`() {
        // Given & When
        val actorDetails = ActorDetailsResponse()

        // Then
        assertThat(actorDetails.id).isNull()
        assertThat(actorDetails.name).isNull()
        assertThat(actorDetails.biography).isNull()
        assertThat(actorDetails.birthday).isNull()
        assertThat(actorDetails.deathday).isNull()
        assertThat(actorDetails.homepage).isNull()
        assertThat(actorDetails.knownForDepartment).isNull()
        assertThat(actorDetails.placeOfBirth).isNull()
        assertThat(actorDetails.profilePath).isNull()
    }

    @Test
    fun `should create ActorDetailsResponse with partial data when some fields are provided`() {
        // When
        val actorDetails = ActorDetailsResponse(
            name = "Scarlett Johansson",
            placeOfBirth = "New York City, USA"
        )

        // Then
        assertThat(actorDetails.id).isNull()
        assertThat(actorDetails.name).isEqualTo("Scarlett Johansson")
        assertThat(actorDetails.biography).isNull()
        assertThat(actorDetails.birthday).isNull()
        assertThat(actorDetails.deathday).isNull()
        assertThat(actorDetails.homepage).isNull()
        assertThat(actorDetails.knownForDepartment).isNull()
        assertThat(actorDetails.placeOfBirth).isEqualTo("New York City, USA")
        assertThat(actorDetails.profilePath).isNull()
    }
}