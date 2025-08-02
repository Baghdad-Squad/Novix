package com.baghdad.remoteDataSource.mapper.episode

import com.baghdad.remoteDataSource.response.tvShow.EpisodeResponse
import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.Test

class EpisodeResponseMapperTest {

    @Test
    fun `should map fields correctly when all values are provided`() {
        // Given
        val response = EpisodeResponse(
            id = 101,
            name = "Pilot Episode",
            episodeNumber = 1,
            voteAverage = 8.5,
            runtime = 45,
            airDate = "2022-10-01",
            seasonNumber = 1,
            overview = "The beginning of an epic journey."
        )

        // When
        val episodeResponseDto = response.toDto()

        // Then
        assertThat(episodeResponseDto.id).isEqualTo(response.id)
        assertThat(episodeResponseDto.title).isEqualTo(response.name)
        assertThat(episodeResponseDto.episodeNumber).isEqualTo(response.episodeNumber)
        assertThat(episodeResponseDto.rating).isEqualTo(response.voteAverage)
        assertThat(episodeResponseDto.duration).isEqualTo(response.runtime.toString())
        assertThat(episodeResponseDto.releasedDate).isEqualTo(response.airDate)
        assertThat(episodeResponseDto.currentSeason).isEqualTo(response.seasonNumber)
        assertThat(episodeResponseDto.overview).isEqualTo(response.overview)
        assertThat(episodeResponseDto.headerPictures).isEmpty()
        assertThat(episodeResponseDto.trailerUrl).isEmpty()
        assertThat(episodeResponseDto.genres).isEmpty()
    }

    @Test
    fun `should use defaults when nullable fields are missing`() {
        // Given
        val response = EpisodeResponse(
            id = null,
            name = null,
            episodeNumber = null,
            voteAverage = null,
            runtime = null,
            airDate = null,
            seasonNumber = null,
            overview = null
        )

        // When
        val episodeResponseDto = response.toDto()

        // Then
        assertThat(episodeResponseDto.id).isEqualTo(0L)
        assertThat(episodeResponseDto.title).isEmpty()
        assertThat(episodeResponseDto.episodeNumber).isEqualTo(0)
        assertThat(episodeResponseDto.rating).isEqualTo(0.0)
        assertThat(episodeResponseDto.duration).isEqualTo("0")
        assertThat(episodeResponseDto.releasedDate).isNull()
        assertThat(episodeResponseDto.currentSeason).isEqualTo(0)
        assertThat(episodeResponseDto.overview).isEmpty()
        assertThat(episodeResponseDto.headerPictures).isEmpty()
        assertThat(episodeResponseDto.trailerUrl).isEmpty()
        assertThat(episodeResponseDto.genres).isEmpty()
    }

    @Test
    fun `should handle partial null fields correctly`() {
        // Given
        val response = EpisodeResponse(
            id = 55,
            name = null,
            episodeNumber = 10,
            voteAverage = null,
            runtime = 60,
            airDate = null,
            seasonNumber = null,
            overview = "Special episode."
        )

        // When
        val episodeResponseDto = response.toDto()

        // Then
        assertThat(episodeResponseDto.id).isEqualTo(response.id)
        assertThat(episodeResponseDto.title).isEmpty()
        assertThat(episodeResponseDto.episodeNumber).isEqualTo(response.episodeNumber)
        assertThat(episodeResponseDto.rating).isEqualTo(0.0)
        assertThat(episodeResponseDto.duration).isEqualTo(response.runtime.toString())
        assertThat(episodeResponseDto.releasedDate).isNull()
        assertThat(episodeResponseDto.currentSeason).isEqualTo(0)
        assertThat(episodeResponseDto.overview).isEqualTo(response.overview)
    }
}
