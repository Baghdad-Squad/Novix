package com.baghdad.repository.mapper

import com.baghdad.entity.person.Actor
import com.baghdad.repository.dummyData.DummyDataFactory.createMockActorDto
import com.baghdad.repository.dummyData.DummyDataFactory.createMockEpisodeDto
import com.google.common.truth.Truth.assertThat
import kotlinx.datetime.LocalDate
import org.junit.jupiter.api.Test

class EpisodeMapperTest {

    @Test
    fun `toEntity should map ActorDto to Actor correctly`() {
        // Given
        val actorDto = createMockActorDto().copy(deathDate = "2020-12-25")

        // When
        val result = actorDto.toEntity()

        // Then
        val expectedResult = Actor(
            id = 789L,
            name = "Test Actor",
            profilePictureURL = "/actor_profile.jpg",
            biography = "Test actor biography",
            birthDate = LocalDate.parse("1985-03-10"),
            deathDate = LocalDate.parse("2020-12-25"),
            placeOfBirth = "Los Angeles, USA",
            headerPictures = listOf("/actor_header1.jpg", "/actor_header2.jpg"),
            department = "Acting"
        )

        assertThat(result).isEqualTo(expectedResult)
    }

    @Test
    fun `should map episodeNumber  when EpisodeDto has different episode numbers`() {
        // Given
        val episodeDto1 = createMockEpisodeDto().copy(episodeNumber = 1)
        val episodeDto2 = createMockEpisodeDto().copy(episodeNumber = 10)
        val episodeDto3 = createMockEpisodeDto().copy(episodeNumber = 100)

        // When
        val result1 = episodeDto1.toEntity()
        val result2 = episodeDto2.toEntity()
        val result3 = episodeDto3.toEntity()

        // Then
        assertThat(result1.episodeNumber).isEqualTo(1)
        assertThat(result2.episodeNumber).isEqualTo(10)
        assertThat(result3.episodeNumber).isEqualTo(100)
    }

    @Test
    fun `should map rating  when EpisodeDto has different ratings`() {
        // Given
        val episodeDto1 = createMockEpisodeDto().copy(rating = 5.0)
        val episodeDto2 = createMockEpisodeDto().copy(rating = 10.0)
        val episodeDto3 = createMockEpisodeDto().copy(rating = 0.0)

        // When
        val result1 = episodeDto1.toEntity()
        val result2 = episodeDto2.toEntity()
        val result3 = episodeDto3.toEntity()

        // Then
        assertThat(result1.rating).isEqualTo(5.0)
        assertThat(result2.rating).isEqualTo(10.0)
        assertThat(result3.rating).isEqualTo(0.0)
    }

    @Test
    fun `should map duration when EpisodeDto has different durations`() {
        // Given
        val episodeDto1 = createMockEpisodeDto().copy(duration = "30 min")
        val episodeDto2 = createMockEpisodeDto().copy(duration = "60 min")
        val episodeDto3 = createMockEpisodeDto().copy(duration = "90 min")

        // When
        val result1 = episodeDto1.toEntity()
        val result2 = episodeDto2.toEntity()
        val result3 = episodeDto3.toEntity()

        // Then
        assertThat(result1.duration).isEqualTo("30 min")
        assertThat(result2.duration).isEqualTo("60 min")
        assertThat(result3.duration).isEqualTo("90 min")
    }

    @Test
    fun `should map currentSeason when EpisodeDto has different seasons`() {
        // Given
        val episodeDto1 = createMockEpisodeDto().copy(currentSeason = 1)
        val episodeDto2 = createMockEpisodeDto().copy(currentSeason = 5)
        val episodeDto3 = createMockEpisodeDto().copy(currentSeason = 10)

        // When
        val result1 = episodeDto1.toEntity()
        val result2 = episodeDto2.toEntity()
        val result3 = episodeDto3.toEntity()

        // Then
        assertThat(result1.currentSeason).isEqualTo(1)
        assertThat(result2.currentSeason).isEqualTo(5)
        assertThat(result3.currentSeason).isEqualTo(10)
    }
} 