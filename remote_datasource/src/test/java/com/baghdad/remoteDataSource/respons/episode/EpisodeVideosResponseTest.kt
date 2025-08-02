package com.baghdad.remoteDataSource.respons.episode

import com.baghdad.remoteDataSource.response.episode.EpisodeVideosResponse
import com.google.common.truth.Truth
import org.junit.jupiter.api.Test

class EpisodeVideosResponseTest {

    @Test
    fun `should create EpisodeVideosResponse with full data when all fields are provided`() {
        // Given
        val expectedResults = listOf(
            EpisodeVideosResponse.Result(
                id = "vid1",
                key = "abc123",
                name = "Official Trailer",
                official = true,
                publishedAt = "2023-12-01",
                site = "YouTube",
                size = 1080,
                type = "Trailer"
            ), EpisodeVideosResponse.Result(
                id = "vid2",
                key = "xyz789",
                name = "Behind the Scenes",
                official = false,
                publishedAt = "2023-12-02",
                site = "Vimeo",
                size = 720,
                type = "Featurette"
            )
        )

        // When
        val response = EpisodeVideosResponse(
            id = 101, results = expectedResults
        )

        // Then
        Truth.assertThat(response.id).isEqualTo(101)
        Truth.assertThat(response.results).isNotNull()
        Truth.assertThat(response.results!!.size).isEqualTo(2)

        val firstResult = response.results[0]
        Truth.assertThat(firstResult!!.id).isEqualTo("vid1")
        Truth.assertThat(firstResult.key).isEqualTo("abc123")
        Truth.assertThat(firstResult.name).isEqualTo("Official Trailer")
        Truth.assertThat(firstResult.official).isTrue()
        Truth.assertThat(firstResult.publishedAt).isEqualTo("2023-12-01")
        Truth.assertThat(firstResult.site).isEqualTo("YouTube")
        Truth.assertThat(firstResult.size).isEqualTo(1080)
        Truth.assertThat(firstResult.type).isEqualTo("Trailer")
    }

    @Test
    fun `should create EpisodeVideosResponse with default values when no fields are provided`() {
        // Given & When
        val response = EpisodeVideosResponse()

        // Then
        Truth.assertThat(response.id).isNull()
        Truth.assertThat(response.results).isNull()
    }

    @Test
    fun `should create Result with default values when no fields are provided`() {
        // Given & When
        val result = EpisodeVideosResponse.Result()

        // Then
        Truth.assertThat(result.id).isNull()
        Truth.assertThat(result.key).isNull()
        Truth.assertThat(result.name).isNull()
        Truth.assertThat(result.official).isNull()
        Truth.assertThat(result.publishedAt).isNull()
        Truth.assertThat(result.site).isNull()
        Truth.assertThat(result.size).isNull()
        Truth.assertThat(result.type).isNull()
    }

    @Test
    fun `should create EpisodeVideosResponse with empty results list when results provided as empty`() {
        // Given
        val response = EpisodeVideosResponse(
            id = 200, results = emptyList()
        )

        // Then
        Truth.assertThat(response.id).isEqualTo(200)
        Truth.assertThat(response.results).isEmpty()
    }

    @Test
    fun `should create Result with partial data when some fields are provided`() {
        // When
        val result = EpisodeVideosResponse.Result(
            name = "Teaser", official = true
        )

        // Then
        Truth.assertThat(result.id).isNull()
        Truth.assertThat(result.key).isNull()
        Truth.assertThat(result.name).isEqualTo("Teaser")
        Truth.assertThat(result.official).isTrue()
        Truth.assertThat(result.publishedAt).isNull()
        Truth.assertThat(result.site).isNull()
        Truth.assertThat(result.size).isNull()
        Truth.assertThat(result.type).isNull()
    }

    @Test
    fun `should handle null result inside results list`() {
        // Given
        val response = EpisodeVideosResponse(
            id = 300, results = listOf(null)
        )

        // Then
        Truth.assertThat(response.id).isEqualTo(300)
        Truth.assertThat(response.results!!.size).isEqualTo(1)
        Truth.assertThat(response.results[0]).isNull()
    }
}