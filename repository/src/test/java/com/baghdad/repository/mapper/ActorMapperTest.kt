package com.baghdad.repository.mapper

import com.baghdad.entity.person.Actor
import com.baghdad.repository.dummyData.DummyDataFactory.DummyDataFactory.createMockActorDto
import com.google.common.truth.Truth.assertThat
import kotlinx.datetime.LocalDate
import org.junit.jupiter.api.Test

class ActorMapperTest {

    @Test
    fun `toEntity should return Actor entity when given valid ActorDto`() {
        // Given
        val actorDto = createMockActorDto()
        // When
        val result = actorDto.toEntity()
        // Then
        val expectedResult = Actor(
            id = 789L,
            name = "Test Actor",
            profilePictureURL = "/actor_profile.jpg",
            biography = "Test actor biography",
            birthDate = LocalDate.parse("1985-03-10"),
            deathDate = null,
            placeOfBirth = "Los Angeles, USA",
            headerPictures = listOf("/actor_header1.jpg", "/actor_header2.jpg"),
            department = "Acting"
        )
        assertThat(result).isEqualTo(expectedResult)
    }

    @Test
    fun `toEntity should return deathDate when Actor is dead`() {
        // Given
        val actorDto = createMockActorDto().copy(deathDate = "2020-12-25")
        // When
        val result = actorDto.toEntity()
        // Then
        assertThat(result.deathDate).isEqualTo(LocalDate.parse("2020-12-25"))
    }
} 