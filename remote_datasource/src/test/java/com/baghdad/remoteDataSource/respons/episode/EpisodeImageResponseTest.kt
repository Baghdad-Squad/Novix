package com.baghdad.remoteDataSource.respons.episode

import com.baghdad.remoteDataSource.response.episode.EpisodeImageResponse
import com.google.common.truth.Truth
import org.junit.jupiter.api.Test

class EpisodeImageResponseTest {

    @Test
    fun `should create EpisodeImageResponse with full data when all fields are provided`() {
        // Given
        val expectedFilePath = "/episode1_still.jpg"
        val expectedEpisodeFrames = listOf(EpisodeImageResponse.EpisodeFrame(filePath = expectedFilePath))

        // When
        val response = EpisodeImageResponse(
            id = 101,
            episodeFrames = expectedEpisodeFrames
        )

        // Then
        Truth.assertThat(response.id).isEqualTo(101)
        Truth.assertThat(response.episodeFrames).isNotNull()
        Truth.assertThat(response.episodeFrames!!.size).isEqualTo(1)
        Truth.assertThat(response.episodeFrames[0].filePath).isEqualTo(expectedFilePath)
    }

    @Test
    fun `should create EpisodeImageResponse with default values when no fields are provided`() {
        // Given & When
        val response = EpisodeImageResponse()

        // Then
        Truth.assertThat(response.id).isNull()
        Truth.assertThat(response.episodeFrames).isNull()
    }

    @Test
    fun `should create Still with default values when no fields are provided`() {
        // Given & When
        val episodeFrame = EpisodeImageResponse.EpisodeFrame()

        // Then
        Truth.assertThat(episodeFrame.filePath).isNull()
    }

    @Test
    fun `should create EpisodeImageResponse with empty stills list when stills provided as empty`() {
        // Given
        val response = EpisodeImageResponse(
            id = 202,
            episodeFrames = emptyList()
        )

        // Then
        Truth.assertThat(response.id).isEqualTo(202)
        Truth.assertThat(response.episodeFrames).isEmpty()
    }

    @Test
    fun `should create Still with partial data when filePath is provided`() {
        // Given
        val expectedFilePath = "/partial_still.jpg"

        // When
        val episodeFrame = EpisodeImageResponse.EpisodeFrame(filePath = expectedFilePath)

        // Then
        Truth.assertThat(episodeFrame.filePath).isEqualTo(expectedFilePath)
    }
}