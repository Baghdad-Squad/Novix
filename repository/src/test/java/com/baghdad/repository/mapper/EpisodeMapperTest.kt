package com.baghdad.repository.mapper

import com.baghdad.repository.model.EpisodeDto
import com.google.common.truth.Truth.assertThat
import kotlinx.datetime.LocalDate
import org.junit.jupiter.api.Test

class EpisodeMapperTest {

    @Test
    fun `EpisodeDto toEntity should map correctly with valid data`() {
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
    fun `EpisodeDto toEntity should handle null released date`() {
        // Given
        val episodeDto = createMockEpisodeDto().copy(releasedDate = null)

        // When
        val result = episodeDto.toEntity()

        // Then
        assertThat(result.releasedDate).isNull()
    }

    @Test
    fun `EpisodeDto toEntity should handle different episode numbers`() {
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
    fun `EpisodeDto toEntity should handle different ratings`() {
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
    fun `EpisodeDto toEntity should handle different durations`() {
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
    fun `EpisodeDto toEntity should handle different seasons`() {
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

    @Test
    fun `EpisodeDto toEntity should handle different titles`() {
        // Given
        val episodeDto1 = createMockEpisodeDto().copy(title = "Pilot")
        val episodeDto2 = createMockEpisodeDto().copy(title = "Season Finale")
        val episodeDto3 = createMockEpisodeDto().copy(title = "Special Episode")

        // When
        val result1 = episodeDto1.toEntity()
        val result2 = episodeDto2.toEntity()
        val result3 = episodeDto3.toEntity()

        // Then
        assertThat(result1.title).isEqualTo("Pilot")
        assertThat(result2.title).isEqualTo("Season Finale")
        assertThat(result3.title).isEqualTo("Special Episode")
    }

    @Test
    fun `EpisodeDto toEntity should handle different overviews`() {
        // Given
        val episodeDto1 = createMockEpisodeDto().copy(overview = "Short overview")
        val episodeDto2 = createMockEpisodeDto().copy(overview = "Very long overview with lots of details...")
        val episodeDto3 = createMockEpisodeDto().copy(overview = "")

        // When
        val result1 = episodeDto1.toEntity()
        val result2 = episodeDto2.toEntity()
        val result3 = episodeDto3.toEntity()

        // Then
        assertThat(result1.overview).isEqualTo("Short overview")
        assertThat(result2.overview).isEqualTo("Very long overview with lots of details...")
        assertThat(result3.overview).isEmpty()
    }

    @Test
    fun `EpisodeDto toEntity should handle empty header pictures`() {
        // Given
        val episodeDto = createMockEpisodeDto().copy(headerPictures = emptyList())

        // When
        val result = episodeDto.toEntity()

        // Then
        assertThat(result.headerPictures).isEmpty()
    }

    @Test
    fun `EpisodeDto toEntity should handle single header picture`() {
        // Given
        val episodeDto = createMockEpisodeDto().copy(headerPictures = listOf("/single_header.jpg"))

        // When
        val result = episodeDto.toEntity()

        // Then
        assertThat(result.headerPictures.size).isEqualTo(1)
        assertThat(result.headerPictures[0]).isEqualTo("/single_header.jpg")
    }

    @Test
    fun `EpisodeDto toEntity should handle genres list`() {
        // Given
        val episodeDto = createMockEpisodeDto().copy(
            genres = listOf(
                createMockGenreDto(35L, "Comedy"),
                createMockGenreDto(18L, "Drama")
            )
        )

        // When
        val result = episodeDto.toEntity()

        // Then
        assertThat(result.genres.size).isEqualTo(2)
        assertThat(result.genres[0].id).isEqualTo(35L)
        assertThat(result.genres[0].name).isEqualTo("Comedy")
        assertThat(result.genres[1].id).isEqualTo(18L)
        assertThat(result.genres[1].name).isEqualTo("Drama")
    }

    @Test
    fun `EpisodeDto toEntity should handle different trailer URLs`() {
        // Given
        val episodeDto1 = createMockEpisodeDto().copy(trailerUrl = "https://youtube.com/watch?v=trailer1")
        val episodeDto2 = createMockEpisodeDto().copy(trailerUrl = "https://vimeo.com/trailer2")
        val episodeDto3 = createMockEpisodeDto().copy(trailerUrl = "")

        // When
        val result1 = episodeDto1.toEntity()
        val result2 = episodeDto2.toEntity()
        val result3 = episodeDto3.toEntity()

        // Then
        assertThat(result1.trailerUrl).isEqualTo("https://youtube.com/watch?v=trailer1")
        assertThat(result2.trailerUrl).isEqualTo("https://vimeo.com/trailer2")
        assertThat(result3.trailerUrl).isEmpty()
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