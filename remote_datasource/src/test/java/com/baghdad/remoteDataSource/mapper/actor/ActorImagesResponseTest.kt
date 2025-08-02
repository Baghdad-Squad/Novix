package com.baghdad.remoteDataSource.mapper.actor

import com.baghdad.remoteDataSource.response.actor.ActorImagesResponse
import com.baghdad.remoteDataSource.response.actor.ImageResponse
import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.Test

class ActorImagesResponseTest {

    @Test
    fun `should create ActorImagesResponse with full data when all fields are provided`() {
        // Given
        val expectedProfiles = listOf(
            ImageResponse(filePath = "/image1.jpg"),
            ImageResponse(filePath = "/image2.jpg")
        )

        // When
        val response = ActorImagesResponse(
            id = 101,
            profiles = expectedProfiles
        )

        // Then
        assertThat(response.id).isEqualTo(101)
        assertThat(response.profiles).isNotNull()
        assertThat(response.profiles).hasSize(2)
        assertThat(response.profiles!![0].filePath).isEqualTo("/image1.jpg")
        assertThat(response.profiles[1].filePath).isEqualTo("/image2.jpg")
    }

    @Test
    fun `should create ActorImagesResponse with default values when no fields are provided`() {
        // Given & When
        val response = ActorImagesResponse()

        // Then
        assertThat(response.id).isNull()
        assertThat(response.profiles).isNull()
    }

    @Test
    fun `should create ActorImagesResponse with empty profiles list when profiles provided as empty`() {
        // Given & When
        val response = ActorImagesResponse(
            id = 202,
            profiles = emptyList()
        )

        // Then
        assertThat(response.id).isEqualTo(202)
        assertThat(response.profiles).isEmpty()
    }

    @Test
    fun `should create ImageResponse with default value when no fields are provided`() {
        // Given & When
        val image = ImageResponse()

        // Then
        assertThat(image.filePath).isNull()
    }

    @Test
    fun `should create ActorImagesResponse with partial data when some fields are provided`() {
        // When
        val response = ActorImagesResponse(
            id = 303
        )

        // Then
        assertThat(response.id).isEqualTo(303)
        assertThat(response.profiles).isNull()
    }

    @Test
    fun `should allow null values for all fields`() {
        // Given & When
        val response = ActorImagesResponse(
            id = null,
            profiles = null
        )

        // Then
        assertThat(response.id).isNull()
        assertThat(response.profiles).isNull()
    }
}