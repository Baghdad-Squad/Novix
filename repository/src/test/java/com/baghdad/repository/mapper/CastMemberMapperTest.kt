package com.baghdad.repository.mapper

import com.baghdad.entity.person.Actor
import com.baghdad.entity.person.CastMember
import com.baghdad.repository.dummyData.DummyDataFactory.DummyDataFactory.createMockActorDto
import com.baghdad.repository.dummyData.DummyDataFactory.DummyDataFactory.createMockCastMemberDto
import com.baghdad.repository.model.CastMemberDto
import com.google.common.truth.Truth.assertThat
import kotlinx.datetime.LocalDate
import org.junit.jupiter.api.Test

class CastMemberMapperTest {

    @Test
    fun `toEntity should return Cast Member entity when given valid CastMemberDto`() {
        // Given
        val castMemberDto = createMockCastMemberDto()

        // When
        val result = castMemberDto.toEntity()

        // Then
        val expectedActor = Actor(
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
        val expectedResult = CastMember(
            actor = expectedActor,
            characterName = "Test Character"
        )
        assertThat(result).isEqualTo(expectedResult)
    }

    @Test
    fun `should return null for death date when actor still a live`() {
        // Given
        val actorDto = createMockActorDto().copy(deathDate = null)
        val castMemberDto = CastMemberDto(actor = actorDto, characterName = "Test Character")

        // When
        val result = castMemberDto.toEntity()

        // Then
        assertThat(result.actor.deathDate).isNull()
        assertThat(result.characterName).isEqualTo("Test Character")
    }

    @Test
    fun `should return empty list for headerPictures when actor has no response pictures`() {
        // Given
        val actorDto = createMockActorDto().copy(headerPictures = emptyList())
        val castMemberDto = CastMemberDto(actor = actorDto, characterName = "Test Character")

        // When
        val result = castMemberDto.toEntity()

        // Then
        assertThat(result.actor.headerPictures).isEmpty()
        assertThat(result.characterName).isEqualTo("Test Character")
    }
}