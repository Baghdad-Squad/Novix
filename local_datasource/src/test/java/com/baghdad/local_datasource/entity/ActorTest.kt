package com.baghdad.local_datasource.entity

import com.baghdad.local_datasource.roomDB.entity.Actor
import com.baghdad.local_datasource.roomDB.entity.toDto
import com.baghdad.local_datasource.roomDB.entity.toEntity
import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.Test

class ActorTest {

    @Test
    fun `should map Actor to ActorDto and back to Actor when toDto and toEntity are called sequentially`() {
        // When
        val actorDto = ACTOR.toDto()

        // Then
        assertThat(actorDto.id).isEqualTo(ACTOR.id)
        assertThat(actorDto.name).isEqualTo(ACTOR.name)
        assertThat(actorDto.headerPictures).isEqualTo(ACTOR.headerPictures)
        assertThat(actorDto.imageUrl).isEqualTo(ACTOR.profilePictureURL)
        assertThat(actorDto.placeOfBirth).isEqualTo(ACTOR.placeOfBirth)
        assertThat(actorDto.deathDate).isEqualTo(ACTOR.deathDate)
        assertThat(actorDto.biography).isEqualTo(ACTOR.biography)
        assertThat(actorDto.birthdayDate).isEqualTo(ACTOR.birthDate)
        assertThat(actorDto.department).isEqualTo(ACTOR.department)
    }

    @Test
    fun `should create Actor with default id`() {
        val actor = Actor(
            name = "Test", profilePictureURL = "url", birthDate = null,
            placeOfBirth = "place", deathDate = null,
            biography = "bio", headerPictures = emptyList(), department = "Acting"
        )
        assertThat(actor.id).isEqualTo(0L)
    }

    @Test
    fun `should preserve non-null deathDate when converting Actor to ActorDto`() {
        // When
        val result = ACTOR.copy(deathDate = "2025").toDto()

        // Then
        assertThat(result.deathDate).isEqualTo("2025")
    }

    @Test
    fun `should handle null deathDate when converting ActorDto to Actor`() {
        // When
        val result = ACTOR.toDto().copy(deathDate = null).toEntity()

        // Then
        assertThat(result.deathDate).isNull()
    }

    @Test
    fun `should return empty headerPictures list when converting Actor with empty headerPictures to ActorDto`() {
        // When
        val result = ACTOR.copy(headerPictures = emptyList()).toDto()

        // Then
        assertThat(result.headerPictures).isEmpty()
    }

    @Test
    fun `should preserve headerPictures order when converting Actor to ActorDto`() {
        // When
        val result = ACTOR.toDto()

        // Then
        assertThat(result.headerPictures)
            .containsExactly("first.jpg", "second.jpg")
            .inOrder()
    }

    companion object {
        val ACTOR = Actor(
            id = 123,
            name = "Test Actor",
            profilePictureURL = "Test URL",
            biography = "Test biography",
            birthDate = "2002-2-22",
            deathDate = null,
            placeOfBirth = "Test place",
            headerPictures = listOf("first.jpg", "second.jpg"),
            department = "Acting"
        )
    }
}
