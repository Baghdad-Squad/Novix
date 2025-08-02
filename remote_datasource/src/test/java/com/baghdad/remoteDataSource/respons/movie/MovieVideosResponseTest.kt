package com.baghdad.remoteDataSource.respons.movie

import com.baghdad.remoteDataSource.response.movie.MovieVideosResponse
import com.google.common.truth.Truth
import org.junit.jupiter.api.Test

class MovieVideosResponseTest {

    @Test
    fun `should create MovieVideosResponse with full data when all fields are provided`() {
        // Given
        val expectedResults = listOf(
            MovieVideosResponse.Result(
                id = "v1",
                key = "abc123",
                name = "Official Trailer",
                official = true,
                publishedAt = "2023-10-01",
                site = "YouTube",
                size = 1080,
                type = "Trailer"
            ),
            MovieVideosResponse.Result(
                id = "v2",
                key = "xyz456",
                name = "Teaser",
                official = false,
                publishedAt = "2023-11-05",
                site = "Vimeo",
                size = 720,
                type = "Teaser"
            )
        )

        // When
        val response = MovieVideosResponse(
            id = 101,
            results = expectedResults
        )

        // Then
        Truth.assertThat(response.id).isEqualTo(101)
        Truth.assertThat(response.results).isNotNull()
        Truth.assertThat(response.results!!.size).isEqualTo(2)

        val first = response.results!![0]
        Truth.assertThat(first!!.id).isEqualTo("v1")
        Truth.assertThat(first.key).isEqualTo("abc123")
        Truth.assertThat(first.name).isEqualTo("Official Trailer")
        Truth.assertThat(first.official).isTrue()
        Truth.assertThat(first.publishedAt).isEqualTo("2023-10-01")
        Truth.assertThat(first.site).isEqualTo("YouTube")
        Truth.assertThat(first.size).isEqualTo(1080)
        Truth.assertThat(first.type).isEqualTo("Trailer")
    }

    @Test
    fun `should create MovieVideosResponse with default values when no fields are provided`() {
        // Given & When
        val response = MovieVideosResponse()

        // Then
        Truth.assertThat(response.id).isNull()
        Truth.assertThat(response.results).isNull()
    }

    @Test
    fun `should create Result with default values when no fields are provided`() {
        // Given & When
        val result = MovieVideosResponse.Result()

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
    fun `should create MovieVideosResponse with empty results list when results provided as empty`() {
        // Given
        val response = MovieVideosResponse(
            id = 200,
            results = emptyList()
        )

        // Then
        Truth.assertThat(response.id).isEqualTo(200)
        Truth.assertThat(response.results).isEmpty()
    }

    @Test
    fun `should create Result with partial data when some fields are provided`() {
        // Given
        val expectedName = "Behind the Scenes"
        val expectedOfficial = true

        // When
        val result = MovieVideosResponse.Result(
            name = expectedName,
            official = expectedOfficial
        )

        // Then
        Truth.assertThat(result.id).isNull()
        Truth.assertThat(result.key).isNull()
        Truth.assertThat(result.name).isEqualTo(expectedName)
        Truth.assertThat(result.official).isTrue()
        Truth.assertThat(result.publishedAt).isNull()
        Truth.assertThat(result.site).isNull()
        Truth.assertThat(result.size).isNull()
        Truth.assertThat(result.type).isNull()
    }

    @Test
    fun `should handle null result inside results list`() {
        // Given
        val response = MovieVideosResponse(
            id = 300,
            results = listOf(null)
        )

        // Then
        Truth.assertThat(response.id).isEqualTo(300)
        Truth.assertThat(response.results!!.size).isEqualTo(1)
        Truth.assertThat(response.results!![0]).isNull()
    }
}