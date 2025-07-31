package com.baghdad.repository.mapper

import com.baghdad.repository.model.ActorDto
import com.baghdad.repository.model.SearchQueryDto
import com.google.common.truth.Truth.assertThat
import kotlinx.datetime.LocalDate
import org.junit.jupiter.api.Test

class ActorMapperTest {

    @Test
    fun `should map correctly when ActorDto has valid data`() {
        // Given
        val actorDto = createMockActorDto()

        // When
        val result = actorDto.toEntity()

        // Then
        assertThat(result.id).isEqualTo(789L)
        assertThat(result.name).isEqualTo("Test Actor")
        assertThat(result.profilePictureURL).isEqualTo("/actor_profile.jpg")
        assertThat(result.biography).isEqualTo("Test actor biography")
        assertThat(result.birthDate).isEqualTo(LocalDate.parse("1985-03-10"))
        assertThat(result.deathDate).isNull()
        assertThat(result.placeOfBirth).isEqualTo("Los Angeles, USA")
        assertThat(result.headerPictures.size).isEqualTo(2)
        assertThat(result.headerPictures[0]).isEqualTo("/actor_header1.jpg")
        assertThat(result.headerPictures[1]).isEqualTo("/actor_header2.jpg")
        assertThat(result.department).isEqualTo("Acting")
    }

    @Test
    fun `should return null birthDate when ActorDto birthday is null`() {
        // Given
        val actorDto = createMockActorDto().copy(birthdayDate = null)

        // When
        val result = actorDto.toEntity()

        // Then
        assertThat(result.birthDate).isNull()
    }


    @Test
    fun `should parse deathDate when ActorDto has valid death date`() {
        val actorDto = createMockActorDto().copy(deathDate = "2020-12-25")
        val result = actorDto.toEntity()

        assertThat(result.deathDate).isEqualTo(LocalDate.parse("2020-12-25"))
    }

    @Test
    fun `should return empty headerPictures when ActorDto has empty list`() {
        // Given
        val actorDto = createMockActorDto().copy(headerPictures = emptyList())
        // When
        val result = actorDto.toEntity()
        // Then
        assertThat(result.headerPictures).isEmpty()
    }

    @Test
    fun `should map to SearchQueryDto correctly when query is provided`() {
        // Given
        val actorDto = createMockActorDto()
        val query = "test actor query"

        // When
        val result = actorDto.toSearchQueryDto(query)

        // Then
        assertThat(result.queryName).isEqualTo(query)
        assertThat(result.mediaId).isEqualTo(actorDto.id)
        assertThat(result.mediaType).isEqualTo(SearchQueryDto.MediaType.ACTOR)
    }

    @Test
    fun `should return correct query values when different queries are used`() {
        // Given
        val actorDto = createMockActorDto()
        val query1 = "action movies"
        val query2 = "comedy films"

        // When
        val result1 = actorDto.toSearchQueryDto(query1)
        val result2 = actorDto.toSearchQueryDto(query2)

        // Then
        assertThat(result1.queryName).isEqualTo(query1)
        assertThat(result1.mediaId).isEqualTo(actorDto.id)
        assertThat(result1.mediaType).isEqualTo(SearchQueryDto.MediaType.ACTOR)
        assertThat(result2.queryName).isEqualTo(query2)
        assertThat(result2.mediaId).isEqualTo(actorDto.id)
        assertThat(result2.mediaType).isEqualTo(SearchQueryDto.MediaType.ACTOR)
    }

    companion object {
        private fun createMockActorDto() = ActorDto(
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