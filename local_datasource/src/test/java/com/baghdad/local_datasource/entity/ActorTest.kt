package com.baghdad.local_datasource.entity

import com.baghdad.local_datasource.roomDB.entity.Actor
import com.baghdad.local_datasource.roomDB.entity.toDto
import com.baghdad.local_datasource.roomDB.entity.toEntity
import com.baghdad.repository.model.ActorDto
import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.Test

class ActorTest {

    @Test
    fun `toDto converts Actor to ActorDto with all fields`() {
        // Given
        val actor = Actor(
            id = 123,
            name = "John Doe",
            profilePictureURL = "https://example.com/john.jpg",
            biography = "A talented actor",
            birthDate = "1980-05-15",
            deathDate = null,
            placeOfBirth = "New York, USA",
            headerPictures = listOf("header1.jpg", "header2.jpg"),
            department = "Acting"
        )

        // When
        val result = actor.toDto()

        // Then
        assertThat(result).isEqualTo(
            ActorDto(
                id = 123,
                name = "John Doe",
                imageUrl = "https://example.com/john.jpg",
                biography = "A talented actor",
                birthdayDate = "1980-05-15",
                deathDate = null,
                placeOfBirth = "New York, USA",
                headerPictures = listOf("header1.jpg", "header2.jpg"),
                department = "Acting"
            )
        )
    }

    @Test
    fun `toEntity converts ActorDto to Actor with all fields`() {
        // Given
        val actorDto = ActorDto(
            id = 456,
            name = "Jane Smith",
            imageUrl = "https://example.com/jane.jpg",
            biography = "An award-winning actress",
            birthdayDate = "1975-11-20",
            deathDate = "2020-01-10",
            placeOfBirth = "London, UK",
            headerPictures = listOf("header3.jpg"),
            department = "Production"
        )

        // When
        val result = actorDto.toEntity()

        // Then
        assertThat(result).isEqualTo(
            Actor(
                id = 456,
                name = "Jane Smith",
                profilePictureURL = "https://example.com/jane.jpg",
                biography = "An award-winning actress",
                birthDate = "1975-11-20",
                deathDate = "2020-01-10",
                placeOfBirth = "London, UK",
                headerPictures = listOf("header3.jpg"),
                department = "Production"
            )
        )
    }

    @Test
    fun `toDto and toEntity are inverse operations`() {
        // Given
        val originalActor = Actor(
            id = 789,
            name = "Original Actor",
            profilePictureURL = "original.jpg",
            biography = "Original bio",
            birthDate = "1990-01-01",
            deathDate = null,
            placeOfBirth = "Original City",
            headerPictures = emptyList(),
            department = "Original Dept"
        )

        // When
        val dto = originalActor.toDto()
        val entity = dto.toEntity()

        // Then
        assertThat(entity).isEqualTo(originalActor)
    }

    @Test
    fun `toDto handles null deathDate correctly`() {
        // Given
        val actor = Actor(
            id = 1,
            name = "No Death Date",
            profilePictureURL = "alive.jpg",
            biography = "Still alive",
            birthDate = "2000-01-01",
            deathDate = null,
            placeOfBirth = "Somewhere",
            headerPictures = emptyList(),
            department = "Acting"
        )

        // When
        val result = actor.toDto()

        // Then
        assertThat(result.deathDate).isNull()
    }

    @Test
    fun `toEntity handles null deathDate correctly`() {
        // Given
        val actorDto = ActorDto(
            id = 2,
            name = "No Death Date DTO",
            imageUrl = "alive_dto.jpg",
            biography = "Still alive in DTO",
            birthdayDate = "2005-01-01",
            deathDate = null,
            placeOfBirth = "Somewhere else",
            headerPictures = listOf("header.jpg"),
            department = "Directing"
        )

        // When
        val result = actorDto.toEntity()

        // Then
        assertThat(result.deathDate).isNull()
    }

    @Test
    fun `toDto handles empty headerPictures correctly`() {
        // Given
        val actor = Actor(
            id = 3,
            name = "No Header Images",
            profilePictureURL = "noheaders.jpg",
            biography = "No header images",
            birthDate = "1995-01-01",
            deathDate = null,
            placeOfBirth = "Nowhere",
            headerPictures = emptyList(),
            department = "Writing"
        )

        // When
        val result = actor.toDto()

        // Then
        assertThat(result.headerPictures).isEmpty()
    }

    @Test
    fun `toDto handles non-empty headerPictures in order`() {
        // Given
        val actor = Actor(
            id = 4,
            name = "With Headers",
            profilePictureURL = "withheaders.jpg",
            biography = "With header images",
            birthDate = "2001-01-01",
            deathDate = null,
            placeOfBirth = "Somewhere",
            headerPictures = listOf("first.jpg", "second.jpg"),
            department = "Directing"
        )

        // When
        val result = actor.toDto()

        // Then
        assertThat(result.headerPictures)
            .containsExactly("first.jpg", "second.jpg")
            .inOrder()
    }
}