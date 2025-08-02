package com.baghdad.remoteDataSource.respons.episode

import com.baghdad.remoteDataSource.response.episode.EpisodeImageResponse
import com.google.common.truth.Truth
import org.junit.jupiter.api.Test

class EpisodeImageResponseTest {

    @Test
    fun `should create EpisodeImageResponse with full data when all fields are provided`() {
        // Given
        val expectedFilePath = "/episode1_still.jpg"
        val expectedStills = listOf(EpisodeImageResponse.Still(filePath = expectedFilePath))

        // When
        val response = EpisodeImageResponse(
            id = 101,
            stills = expectedStills
        )

        // Then
        Truth.assertThat(response.id).isEqualTo(101)
        Truth.assertThat(response.stills).isNotNull()
        Truth.assertThat(response.stills!!.size).isEqualTo(1)
        Truth.assertThat(response.stills[0].filePath).isEqualTo(expectedFilePath)
    }

    @Test
    fun `should create EpisodeImageResponse with default values when no fields are provided`() {
        // Given & When
        val response = EpisodeImageResponse()

        // Then
        Truth.assertThat(response.id).isNull()
        Truth.assertThat(response.stills).isNull()
    }

    @Test
    fun `should create Still with default values when no fields are provided`() {
        // Given & When
        val still = EpisodeImageResponse.Still()

        // Then
        Truth.assertThat(still.filePath).isNull()
    }

    @Test
    fun `should create EpisodeImageResponse with empty stills list when stills provided as empty`() {
        // Given
        val response = EpisodeImageResponse(
            id = 202,
            stills = emptyList()
        )

        // Then
        Truth.assertThat(response.id).isEqualTo(202)
        Truth.assertThat(response.stills).isEmpty()
    }

    @Test
    fun `should create Still with partial data when filePath is provided`() {
        // Given
        val expectedFilePath = "/partial_still.jpg"

        // When
        val still = EpisodeImageResponse.Still(filePath = expectedFilePath)

        // Then
        Truth.assertThat(still.filePath).isEqualTo(expectedFilePath)
    }
}