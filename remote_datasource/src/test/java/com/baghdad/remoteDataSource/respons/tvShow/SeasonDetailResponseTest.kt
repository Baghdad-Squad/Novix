package com.baghdad.remoteDataSource.respons.tvShow

import com.baghdad.remoteDataSource.response.tvShow.EpisodeResponse
import com.baghdad.remoteDataSource.response.tvShow.SeasonDetailResponse
import com.google.common.truth.Truth
import org.junit.jupiter.api.Test

class SeasonDetailResponseTest {

    @Test
    fun `should create SeasonDetailResponse with full episodes data`() {
        // Given
        val episodes = listOf(
            EpisodeResponse(
                airDate = "2023-05-01",
                episodeNumber = 1,
                id = 101,
                name = "Pilot",
                overview = "The beginning of everything.",
                runtime = 50,
                seasonNumber = 1,
                voteAverage = 8.5
            ),
            EpisodeResponse(
                airDate = "2023-05-08",
                episodeNumber = 2,
                id = 102,
                name = "Second Chance",
                overview = "The story continues.",
                runtime = 55,
                seasonNumber = 1,
                voteAverage = 8.7
            )
        )

        // When
        val response = SeasonDetailResponse(episodes = episodes)

        // Then
        Truth.assertThat(response.episodes).isNotNull()
        Truth.assertThat(response.episodes).hasSize(2)
        val firstEpisode = response.episodes!![0]
        Truth.assertThat(firstEpisode.name).isEqualTo("Pilot")
        Truth.assertThat(firstEpisode.voteAverage).isEqualTo(8.5)
    }

    @Test
    fun `should create SeasonDetailResponse with default values`() {
        // When
        val response = SeasonDetailResponse()

        // Given
        Truth.assertThat(response.episodes).isNull()
    }

    @Test
    fun `should create EpisodeResponse with default values`() {
        // When
        val episode = EpisodeResponse()

        // Given
        Truth.assertThat(episode.airDate).isNull()
        Truth.assertThat(episode.episodeNumber).isEqualTo(0)
        Truth.assertThat(episode.id).isEqualTo(0)
        Truth.assertThat(episode.name).isNull()
        Truth.assertThat(episode.overview).isNull()
        Truth.assertThat(episode.runtime).isEqualTo(0)
        Truth.assertThat(episode.seasonNumber).isEqualTo(0)
        Truth.assertThat(episode.voteAverage).isEqualTo(0.0)
    }

    @Test
    fun `should create SeasonDetailResponse with empty episodes list`() {
        //When
        val response = SeasonDetailResponse(episodes = emptyList())
        // Then
        Truth.assertThat(response.episodes).isEmpty()
    }

    @Test
    fun `should create EpisodeResponse with partial data`() {
        // Given
        val episode = EpisodeResponse(
            name = "Finale",
            voteAverage = 9.0
        )

        // Then
        Truth.assertThat(episode.name).isEqualTo("Finale")
        Truth.assertThat(episode.voteAverage).isEqualTo(9.0)
        Truth.assertThat(episode.id).isEqualTo(0)
        Truth.assertThat(episode.airDate).isNull()
    }

    @Test
    fun `should allow null values for all fields in EpisodeResponse`() {
        // Given & When
        val episode = EpisodeResponse(
            airDate = null,
            episodeNumber = null,
            id = null,
            name = null,
            overview = null,
            runtime = null,
            seasonNumber = null,
            voteAverage = null
        )

        // Then
        Truth.assertThat(episode.airDate).isNull()
        Truth.assertThat(episode.episodeNumber).isNull()
        Truth.assertThat(episode.id).isNull()
        Truth.assertThat(episode.runtime).isNull()
        Truth.assertThat(episode.voteAverage).isNull()
    }
}