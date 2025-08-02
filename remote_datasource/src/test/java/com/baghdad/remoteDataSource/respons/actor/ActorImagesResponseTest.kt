package com.baghdad.remoteDataSource.respons.actor

import com.baghdad.remoteDataSource.response.actor.ActorImagesResponse
import com.baghdad.remoteDataSource.response.actor.ImageResponse
import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.Test

class ActorImagesResponseTest {

    @Test
    fun `should create ActorImagesResponse with full data when all fields are provided`() {
        // When
        val actorImagesResponse = ActorImagesResponse(
            id = 55,
            profiles = listOf(
                ImageResponse(filePath = "/images/actor1.jpg"),
                ImageResponse(filePath = "/images/actor2.jpg")
            )
        )

        // Then
        assertThat(actorImagesResponse.id).isEqualTo(55)
        assertThat(actorImagesResponse.profiles).isNotNull()
        assertThat(actorImagesResponse.profiles!!.size).isEqualTo(2)
        assertThat(actorImagesResponse.profiles[0].filePath).isEqualTo("/images/actor1.jpg")
        assertThat(actorImagesResponse.profiles[1].filePath).isEqualTo("/images/actor2.jpg")
    }

    @Test
    fun `should create ActorImagesResponse with default values when no fields are provided`() {
        // Given & When
        val actorImagesResponse = ActorImagesResponse()

        // Then
        assertThat(actorImagesResponse.id).isNull()
        assertThat(actorImagesResponse.profiles).isNull()
    }

    @Test
    fun `should create ImageResponse with filePath when provided`() {
        // Given
        val expectedPath = "/images/actor_profile.jpg"

        // When
        val imageResponse = ImageResponse(filePath = expectedPath)

        // Then
        assertThat(imageResponse.filePath).isEqualTo(expectedPath)
    }

    @Test
    fun `should create ImageResponse with default values when no fields are provided`() {
        // Given & When
        val imageResponse = ImageResponse()

        // Then
        assertThat(imageResponse.filePath).isNull()
    }

    @Test
    fun `should create ActorImagesResponse with empty profiles list when profiles provided as empty`() {
        // When
        val actorImagesResponse = ActorImagesResponse(
            id = 77,
            profiles = emptyList<ImageResponse>()
        )

        // Then
        assertThat(actorImagesResponse.id).isEqualTo(77)
        assertThat(actorImagesResponse.profiles).isEmpty()
    }
}