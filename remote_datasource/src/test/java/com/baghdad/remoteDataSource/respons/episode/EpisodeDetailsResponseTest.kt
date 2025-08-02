package com.baghdad.remoteDataSource.respons.episode

import com.baghdad.remoteDataSource.response.episode.EpisodeDetailsResponse
import com.google.common.truth.Truth
import org.junit.jupiter.api.Test

class EpisodeDetailsResponseTest {

    @Test
    fun `should create EpisodeDetailsResponse with full data when all fields are provided`() {
        // Given
        val expectedAirDate = "2023-12-01"
        val expectedEpisodeNumber = 1
        val expectedName = "Pilot"
        val expectedOverview = "The very first episode."
        val expectedId = 123
        val expectedRuntime = 55
        val expectedSeasonNumber = 1
        val expectedVoteAverage = 8.7

        // When
        val episode = EpisodeDetailsResponse(
            airDate = expectedAirDate,
            episodeNumber = expectedEpisodeNumber,
            name = expectedName,
            overview = expectedOverview,
            id = expectedId,
            runtime = expectedRuntime,
            seasonNumber = expectedSeasonNumber,
            voteAverage = expectedVoteAverage
        )

        // Then
        Truth.assertThat(episode.airDate).isEqualTo(expectedAirDate)
        Truth.assertThat(episode.episodeNumber).isEqualTo(expectedEpisodeNumber)
        Truth.assertThat(episode.name).isEqualTo(expectedName)
        Truth.assertThat(episode.overview).isEqualTo(expectedOverview)
        Truth.assertThat(episode.id).isEqualTo(expectedId)
        Truth.assertThat(episode.runtime).isEqualTo(expectedRuntime)
        Truth.assertThat(episode.seasonNumber).isEqualTo(expectedSeasonNumber)
        Truth.assertThat(episode.voteAverage).isEqualTo(expectedVoteAverage)
    }

    @Test
    fun `should create EpisodeDetailsResponse with default values when no fields are provided`() {
        // Given & When
        val episode = EpisodeDetailsResponse()

        // Then
        Truth.assertThat(episode.airDate).isNull()
        Truth.assertThat(episode.episodeNumber).isEqualTo(0)
        Truth.assertThat(episode.name).isNull()
        Truth.assertThat(episode.overview).isNull()
        Truth.assertThat(episode.id).isEqualTo(0)
        Truth.assertThat(episode.runtime).isEqualTo(0)
        Truth.assertThat(episode.seasonNumber).isEqualTo(0)
        Truth.assertThat(episode.voteAverage).isEqualTo(0.0)
    }

    @Test
    fun `should create EpisodeDetailsResponse with partial data when some fields are provided`() {
        // When
        val episode = EpisodeDetailsResponse(
            name = "Final Episode",
            voteAverage = 9.3
        )

        // Then
        Truth.assertThat(episode.airDate).isNull()
        Truth.assertThat(episode.episodeNumber).isEqualTo(0)
        Truth.assertThat(episode.name).isEqualTo("Final Episode")
        Truth.assertThat(episode.overview).isNull()
        Truth.assertThat(episode.id).isEqualTo(0)
        Truth.assertThat(episode.runtime).isEqualTo(0)
        Truth.assertThat(episode.seasonNumber).isEqualTo(0)
        Truth.assertThat(episode.voteAverage).isEqualTo(9.3)
    }
}