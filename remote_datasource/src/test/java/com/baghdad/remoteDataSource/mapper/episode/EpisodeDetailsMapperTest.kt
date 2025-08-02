package com.baghdad.remoteDataSource.mapper.episode

import com.baghdad.remoteDataSource.response.episode.EpisodeDetailsResponse
import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.Test

class EpisodeDetailsMapperTest {

    @Test
    fun `should map fields correctly when all values are provided`() {
        // Given
        val response = EpisodeDetailsResponse(
            id = 200,
            name = "Final Episode",
            episodeNumber = 10,
            voteAverage = 9.2,
            runtime = 55,
            airDate = "2023-09-10",
            seasonNumber = 2,
            overview = "The big finale!"
        )

        // When
        val dto = response.toDto()

        // Then
        assertThat(dto.id).isEqualTo(response.id)
        assertThat(dto.title).isEqualTo(response.name)
        assertThat(dto.episodeNumber).isEqualTo(response.episodeNumber)
        assertThat(dto.rating).isEqualTo(response.voteAverage)
        assertThat(dto.duration).isEqualTo(response.runtime.toString())
        assertThat(dto.releasedDate).isEqualTo(response.airDate)
        assertThat(dto.currentSeason).isEqualTo(response.seasonNumber)
        assertThat(dto.overview).isEqualTo(response.overview)
        assertThat(dto.headerPictures).isEmpty()
        assertThat(dto.trailerUrl).isEmpty()
        assertThat(dto.genres).isEmpty()
    }

    @Test
    fun `should use safe defaults when fields contain edge cases`() {
        // Given
        val response = EpisodeDetailsResponse(
            id = 0,
            name = null,
            episodeNumber = 0,
            voteAverage = 0.0,
            runtime = 0,
            airDate = null,
            seasonNumber = 0,
            overview = null
        )

        // When
        val dto = response.toDto()

        // Then
        assertThat(dto.id).isEqualTo(0L)
        assertThat(dto.title).isEmpty()
        assertThat(dto.episodeNumber).isEqualTo(0)
        assertThat(dto.rating).isEqualTo(0.0)
        assertThat(dto.duration).isEqualTo("0")
        assertThat(dto.releasedDate).isNull()
        assertThat(dto.currentSeason).isEqualTo(0)
        assertThat(dto.overview).isEmpty()
    }


    @Test
    fun `should handle empty overview safely`() {
        // Given
        val response = EpisodeDetailsResponse(
            id = 300,
            name = "Overview Test",
            episodeNumber = 1,
            voteAverage = 6.0,
            runtime = 50,
            airDate = null,
            seasonNumber = 1,
            overview = null
        )

        // When
        val dto = response.toDto()

        // Then
        assertThat(dto.overview).isEmpty()
        assertThat(dto.releasedDate).isNull()
    }
}
