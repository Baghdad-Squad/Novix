package com.baghdad.repository.mapper

import com.baghdad.repository.dummyData.DummyDataFactory.createMockCastMemberDto
import com.baghdad.repository.model.CastMemberDto
import com.google.common.truth.Truth.assertThat
import kotlinx.datetime.LocalDate
import org.junit.jupiter.api.Test

class CastMemberMapperTest {

    @Test
    fun `should map correctly when CastMemberDto has valid data`() {
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
    fun `should return null for death date when actor has null deathDate`() {
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
    fun `should map valid death date correctly when actor has death date`() {
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
    fun `should return empty list for headerPictures when actor has none`() {
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
    fun `should map single header picture correctly when actor has one header image`() {
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