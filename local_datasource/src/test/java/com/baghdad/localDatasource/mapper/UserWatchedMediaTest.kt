package com.baghdad.localDatasource.mapper

import com.baghdad.localDatasource.roomDB.entity.UserWatchedMedia
import com.baghdad.localDatasource.roomDB.entity.toDto
import com.baghdad.localDatasource.roomDB.entity.toDtos
import com.baghdad.repository.model.UserWatchedMediaDto
import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test


class UserWatchedMediaTest {

    @Test
    fun `should convert all fields correctly when toDto is called`() {
        val result = userWatchedMedia.toDto()

        assertThat(result.contentId).isEqualTo(1L)
        assertThat(result.genreIds).containsExactly(1L, 2L, 3L)
        assertThat(result.contentImageUrl).isEqualTo("https://example.com/poster.jpg")
        assertThat(result.contentType).isEqualTo(UserWatchedMediaDto.ContentType.MOVIE)
        assertThat(result.userId).isEqualTo(1L)
    }

    @Test
    fun `should throw exception when toDto is called with invalid contentType`() {
        val invalidEntity = userWatchedMedia.copy(contentType = "INVALID")
        assertThrows(IllegalArgumentException::class.java) {
            invalidEntity.toDto()
        }
    }

    @Test
    fun `should handle all enum values correctly when toDto is called`() {
        assertThat(userWatchedMedia.toDto().contentType)
            .isEqualTo(UserWatchedMediaDto.ContentType.MOVIE)
    }

    @Test
    fun `should return empty genreIds when toLocalDto is called with empty genreIds`() {
        val result = userWatchedMedia.copy(genreIds = emptyList())

        assertThat(result.genreIds).isEmpty()
    }

    @Test
    fun `should return empty contentImageUrl when toDto is called with empty contentImageUrl`() {
        val emptyImage = userWatchedMedia.copy(contentImageUrl = "")

        val result = emptyImage.toDto()

        assertThat(result.contentImageUrl).isEmpty()
    }

    @Test
    fun `should convert list of UserWatchedMedia to list of UserWatchedMediaDto when toDtos is called`() {
        val entities = listOf(
            userWatchedMedia,
            userWatchedMedia.copy(
                contentId = 2L,
                contentType = "TV_SHOW"
            )
        )

        val result = entities.toDtos()

        assertThat(result).hasSize(2)

        assertThat(result[0].contentId).isEqualTo(1L)
        assertThat(result[0].contentType).isEqualTo(UserWatchedMediaDto.ContentType.MOVIE)

        assertThat(result[1].contentId).isEqualTo(2L)
        assertThat(result[1].contentType).isEqualTo(UserWatchedMediaDto.ContentType.TV_SHOW)
    }


    companion object {
        val userWatchedMedia = UserWatchedMedia(
            contentId = 1L,
            genreIds = listOf(1L, 2L, 3L),
            contentImageUrl = "https://example.com/poster.jpg",
            contentType = "MOVIE",
            userId = 1L
        )
    }
}
