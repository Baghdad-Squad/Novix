package com.baghdad.repository.mapper

import com.baghdad.repository.dummyData.DummyDataFactory.createMockCastMemberDto
import com.baghdad.repository.model.CastMemberDto
import com.google.common.truth.Truth.assertThat
import kotlinx.datetime.LocalDate
import org.junit.jupiter.api.Test

class CastMemberMapperTest {

    @Test
    fun `CastMemberDto toEntity should map correctly with valid data`() {
        // Given
        val castMemberDto = createMockCastMemberDto()

        // When
        val result = castMemberDto.toEntity()

        // Then
        assertThat(result.actor.id).isEqualTo(789L)
        assertThat(result.actor.name).isEqualTo("Test Actor")
        assertThat(result.actor.profilePictureURL).isEqualTo("/actor_profile.jpg")
        assertThat(result.actor.biography).isEqualTo("Test actor biography")
        assertThat(result.actor.birthDate).isEqualTo(LocalDate.parse("1985-03-10"))
        assertThat(result.actor.deathDate).isNull()
        assertThat(result.actor.placeOfBirth).isEqualTo("Los Angeles, USA")
        assertThat(result.actor.headerPictures.size).isEqualTo(2)
        assertThat(result.actor.headerPictures[0]).isEqualTo("/actor_header1.jpg")
        assertThat(result.actor.headerPictures[1]).isEqualTo("/actor_header2.jpg")
        assertThat(result.actor.department).isEqualTo("Acting")
        assertThat(result.characterName).isEqualTo("Test Character")
    }

    @Test
    fun `CastMemberDto toEntity should handle different character names`() {
        // Given
        val castMemberDto1 = createMockCastMemberDto().copy(characterName = "James Bond")
        val castMemberDto2 = createMockCastMemberDto().copy(characterName = "Iron Man")
        val castMemberDto3 = createMockCastMemberDto().copy(characterName = "")

        // When
        val result1 = castMemberDto1.toEntity()
        val result2 = castMemberDto2.toEntity()
        val result3 = castMemberDto3.toEntity()

        // Then
        assertThat(result1.characterName).isEqualTo("James Bond")
        assertThat(result2.characterName).isEqualTo("Iron Man")
        assertThat(result3.characterName).isEmpty()
    }

    @Test
    fun `CastMemberDto toEntity should handle different actors`() {
        // Given
        val actorDto1 = createMockActorDto().copy(
            id = 123L,
            name = "Actor One",
        )
        val actorDto2 = createMockActorDto().copy(
            id = 456L,
            name = "Actor Two",
        )
        val castMemberDto1 = CastMemberDto(actor = actorDto1, characterName = "Character One")
        val castMemberDto2 = CastMemberDto(actor = actorDto2, characterName = "Character Two")

        // When
        val result1 = castMemberDto1.toEntity()
        val result2 = castMemberDto2.toEntity()

        // Then
        assertThat(result1.actor.id).isEqualTo(123L)
        assertThat(result1.actor.name).isEqualTo("Actor One")
        assertThat(result1.characterName).isEqualTo("Character One")
        assertThat(result2.actor.id).isEqualTo(456L)
        assertThat(result2.actor.name).isEqualTo("Actor Two")
        assertThat(result2.characterName).isEqualTo("Character Two")
    }

    @Test
    fun `CastMemberDto toEntity should handle actor with null death date`() {
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
    fun `CastMemberDto toEntity should handle actor with valid death date`() {
        // Given
        val actorDto = createMockActorDto().copy(deathDate = "2020-12-25")
        val castMemberDto = CastMemberDto(actor = actorDto, characterName = "Test Character")

        // When
        val result = castMemberDto.toEntity()

        // Then
        assertThat(result.actor.deathDate).isEqualTo(LocalDate.parse("2020-12-25"))
        assertThat(result.characterName).isEqualTo("Test Character")
    }

    @Test
    fun `CastMemberDto toEntity should handle actor with empty header pictures`() {
        // Given
        val actorDto = createMockActorDto().copy(headerPictures = emptyList())
        val castMemberDto = CastMemberDto(actor = actorDto, characterName = "Test Character")

        // When
        val result = castMemberDto.toEntity()

        // Then
        assertThat(result.actor.headerPictures).isEmpty()
        assertThat(result.characterName).isEqualTo("Test Character")
    }

    @Test
    fun `CastMemberDto toEntity should handle actor with single header picture`() {
        // Given
        val actorDto = createMockActorDto().copy(headerPictures = listOf("/single_header.jpg"))
        val castMemberDto = CastMemberDto(actor = actorDto, characterName = "Test Character")

        // When
        val result = castMemberDto.toEntity()

        // Then
        assertThat(result.actor.headerPictures.size).isEqualTo(1)
        assertThat(result.actor.headerPictures[0]).isEqualTo("/single_header.jpg")
        assertThat(result.characterName).isEqualTo("Test Character")
    }

    companion object {
        private fun createMockActorDto() = com.baghdad.repository.model.ActorDto(
            id = 789L,
            name = "Test Actor",
            imageUrl = "/actor_profile.jpg",
            biography = "Test actor biography",
            birthdayDate = "1985-03-10",
            deathDate = null,
            placeOfBirth = "Los Angeles, USA",
            headerPictures = listOf("/actor_header1.jpg", "/actor_header2.jpg"),
            department = "Acting"
        )
    }
} 