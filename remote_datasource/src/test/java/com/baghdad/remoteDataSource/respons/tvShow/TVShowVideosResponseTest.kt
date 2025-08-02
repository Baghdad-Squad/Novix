package com.baghdad.remoteDataSource.respons.tvShow

import com.baghdad.remoteDataSource.response.tvShow.TVShowVideosResponse
import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.Test

class TVShowVideosResponseTest {

    @Test
    fun `should create TVShowVideosResponse with full data when all fields are provided`() {
        // Given
        val expectedResults = listOf(
            TVShowVideosResponse.Result(
                id = "vid1",
                key = "abcd1234",
                name = "Official Trailer",
                official = true,
                publishedAt = "2023-07-01",
                site = "YouTube",
                size = 1080,
                type = "Trailer"
            ),
            TVShowVideosResponse.Result(
                id = "vid2",
                key = "efgh5678",
                name = "Behind the Scenes",
                official = false,
                publishedAt = "2023-07-10",
                site = "YouTube",
                size = 720,
                type = "Featurette"
            )
        )

        // When
        val response = TVShowVideosResponse(
            id = 101,
            results = expectedResults
        )

        // Then
        assertThat(response.id).isEqualTo(101)
        assertThat(response.results).isNotNull()
        assertThat(response.results!!.size).isEqualTo(2)

        val firstResult = response.results.first()
        assertThat(firstResult?.id).isEqualTo("vid1")
        assertThat(firstResult?.key).isEqualTo("abcd1234")
        assertThat(firstResult?.name).isEqualTo("Official Trailer")
        assertThat(firstResult?.official).isTrue()
        assertThat(firstResult?.publishedAt).isEqualTo("2023-07-01")
        assertThat(firstResult?.site).isEqualTo("YouTube")
        assertThat(firstResult?.size).isEqualTo(1080)
        assertThat(firstResult?.type).isEqualTo("Trailer")
    }

    @Test
    fun `should create TVShowVideosResponse with default values when no fields are provided`() {
        // When
        val response = TVShowVideosResponse()

        // Then
        assertThat(response.id).isNull()
        assertThat(response.results).isNull()
    }

    @Test
    fun `should create TVShowVideosResponse with empty results when provided`() {
        // When
        val response = TVShowVideosResponse(
            id = 202,
            results = emptyList()
        )

        // Then
        assertThat(response.id).isEqualTo(202)
        assertThat(response.results).isEmpty()
    }

    @Test
    fun `should create Result with partial data when some fields are provided`() {
        // When
        val result = TVShowVideosResponse.Result(
            id = "vid3",
            name = "Teaser Trailer"
        )

        // Then
        assertThat(result.id).isEqualTo("vid3")
        assertThat(result.name).isEqualTo("Teaser Trailer")
        assertThat(result.key).isNull()
        assertThat(result.official).isNull()
        assertThat(result.publishedAt).isNull()
        assertThat(result.site).isNull()
        assertThat(result.size).isNull()
        assertThat(result.type).isNull()
    }
}
