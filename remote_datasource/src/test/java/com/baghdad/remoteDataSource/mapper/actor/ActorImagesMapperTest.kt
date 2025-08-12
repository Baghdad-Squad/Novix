package com.baghdad.remoteDataSource.mapper.actor

import com.baghdad.remoteDataSource.response.actor.ActorImagesResponse
import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.Test

class ActorImagesMapperTest {

    companion object {
        private val COMPLETE_IMAGES_RESPONSE = ActorImagesResponse(
            profiles = listOf(
                ActorImagesResponse.ImageResponse(filePath = "/image1.jpg"),
                ActorImagesResponse.ImageResponse(filePath = "/image2.jpg"),
                ActorImagesResponse.ImageResponse(filePath = "/image3.jpg")
            )
        )

        private val NULL_IMAGES_RESPONSE = ActorImagesResponse(
            profiles = null
        )

        private val EMPTY_IMAGES_RESPONSE = ActorImagesResponse(
            profiles = emptyList()
        )

        private val MIXED_FILE_PATHS_RESPONSE = ActorImagesResponse(
            profiles = listOf(
                ActorImagesResponse.ImageResponse(filePath = "/valid_image.jpg"),
                ActorImagesResponse.ImageResponse(filePath = null),
                ActorImagesResponse.ImageResponse(filePath = ""),
                ActorImagesResponse.ImageResponse(filePath = "/another_valid.jpg")
            )
        )

        private val ALL_NULL_FILE_PATHS_RESPONSE = ActorImagesResponse(
            profiles = listOf(
                ActorImagesResponse.ImageResponse(filePath = null),
                ActorImagesResponse.ImageResponse(filePath = null),
                ActorImagesResponse.ImageResponse(filePath = null)
            )
        )

        private val ALL_EMPTY_FILE_PATHS_RESPONSE = ActorImagesResponse(
            profiles = listOf(
                ActorImagesResponse.ImageResponse(filePath = ""),
                ActorImagesResponse.ImageResponse(filePath = ""),
                ActorImagesResponse.ImageResponse(filePath = "")
            )
        )

        private val EXPECTED_COMPLETE_URLS = listOf(
            "https://image.tmdb.org/t/p/w500/image1.jpg",
            "https://image.tmdb.org/t/p/w500/image2.jpg",
            "https://image.tmdb.org/t/p/w500/image3.jpg"
        )

        private val EXPECTED_EMPTY_LIST = emptyList<String>()

        private val EXPECTED_MIXED_URLS = listOf(
            "https://image.tmdb.org/t/p/w500/valid_image.jpg",
            "https://image.tmdb.org/t/p/w500/another_valid.jpg"
        )
    }

    @Test
    fun `should convert all valid profiles to image URLs`() {
        val imagesResponse = COMPLETE_IMAGES_RESPONSE

        val result = imagesResponse.toActorDtoList()

        assertThat(result).isEqualTo(EXPECTED_COMPLETE_URLS)
    }

    @Test
    fun `should return empty list when profiles is null`() {
        val imagesResponse = NULL_IMAGES_RESPONSE

        val result = imagesResponse.toActorDtoList()

        assertThat(result).isEqualTo(EXPECTED_EMPTY_LIST)
    }

    @Test
    fun `should return empty list when profiles is empty`() {
        val imagesResponse = EMPTY_IMAGES_RESPONSE

        val result = imagesResponse.toActorDtoList()

        assertThat(result).isEqualTo(EXPECTED_EMPTY_LIST)
    }

    @Test
    fun `should filter out null and empty file paths`() {
        val imagesResponse = MIXED_FILE_PATHS_RESPONSE

        val result = imagesResponse.toActorDtoList()

        assertThat(result).isEqualTo(EXPECTED_MIXED_URLS)
    }

    @Test
    fun `should return empty list when all file paths are null`() {
        val imagesResponse = ALL_NULL_FILE_PATHS_RESPONSE

        val result = imagesResponse.toActorDtoList()

        assertThat(result).isEqualTo(EXPECTED_EMPTY_LIST)
    }

    @Test
    fun `should return empty list when all file paths are empty`() {
        val imagesResponse = ALL_EMPTY_FILE_PATHS_RESPONSE

        val result = imagesResponse.toActorDtoList()

        assertThat(result).isEqualTo(EXPECTED_EMPTY_LIST)
    }
}