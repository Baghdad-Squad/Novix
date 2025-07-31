package com.baghdad.repository.mapper

import com.baghdad.repository.model.EpisodeDto
import com.google.common.truth.Truth.assertThat
import kotlinx.datetime.LocalDate
import org.junit.jupiter.api.Test

class EpisodeMapperTest {

    @Test
    fun `should map correctly to entity when EpisodeDto has valid data`() {
        // Given
        val episodeDto = createMockEpisodeDto()

        // When
        val result = episodeDto.toEntity()

        // Then
        assertThat(result.id).isEqualTo(456L)
        assertThat(result.title).isEqualTo("Test Episode")
        assertThat(result.episodeNumber).isEqualTo(5)
        assertThat(result.rating).isEqualTo(8.5)
        assertThat(result.duration).isEqualTo("45 min")
        assertThat(result.releasedDate).isEqualTo(LocalDate.parse("2023-05-15"))
        assertThat(result.currentSeason).isEqualTo(2)
        assertThat(result.overview).isEqualTo("Test episode overview")
        assertThat(result.headerPictures.size).isEqualTo(2)
        assertThat(result.headerPictures[0]).isEqualTo("/header1.jpg")
        assertThat(result.headerPictures[1]).isEqualTo("/header2.jpg")
        assertThat(result.trailerUrl).isEqualTo(" ")
        assertThat(result.genres).isEmpty()
    }

    @Test
    fun `should return null releasedDate when EpisodeDto has null releasedDate`() {
        // Given
        val episodeDto = createMockEpisodeDto().copy(releasedDate = null)

        // When
        val result = episodeDto.toEntity()

        // Then
        assertThat(result.releasedDate).isNull()
    }

    @Test
    fun `should map episodeNumber correctly when EpisodeDto has different episode numbers`() {
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
    fun `should map rating correctly when EpisodeDto has different ratings`() {
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
    fun `should map duration correctly when EpisodeDto has different durations`() {
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
    fun `should map currentSeason correctly when EpisodeDto has different seasons`() {
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

    companion object {
        private fun createMockEpisodeDto() = EpisodeDto(
            id = 456L,
            title = "Test Episode",
            episodeNumber = 5,
            rating = 8.5,
            duration = "45 min",
            releasedDate = "2023-05-15",
            currentSeason = 2,
            overview = "Test episode overview",
            headerPictures = listOf("/header1.jpg", "/header2.jpg"),
            trailerUrl = " ",
            genres = emptyList()
        )

        private fun createMockGenreDto(id: Long, name: String) = com.baghdad.repository.model.GenreDto(
            id = id,
            name = name,
            type = com.baghdad.repository.model.GenreDto.GenreType.TV_SHOW
        )
    }
} 