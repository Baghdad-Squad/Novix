package com.baghdad.repository.mapper

import com.baghdad.domain.model.continueWatching.UserWatchedMedia
import com.baghdad.repository.model.UserWatchedMediaDto
import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.Test

class UserWatchedMediaMapperTest {

    @Test
    fun `should map to entity correctly when dto has valid data`() {
        val userWatchedMediaDto = createMockUserWatchedMediaDto()

        val result = userWatchedMediaDto.toEntity(
            isSaved = false,
            listId = 1
        )

        assertThat(result.contentId).isEqualTo(123L)
        assertThat(result.genreIds.size).isEqualTo(2)
        assertThat(result.genreIds[0]).isEqualTo(28L)
        assertThat(result.genreIds[1]).isEqualTo(12L)
        assertThat(result.contentImageUrl).isEqualTo("/content_image.jpg")
        assertThat(result.contentType).isEqualTo(UserWatchedMedia.ContentType.MOVIE)
        assertThat(result.userId).isEqualTo(123)
    }

    @Test
    fun `should map to TV_SHOW content type when dto is TVSHOW`() {
        val userWatchedMediaDto = createMockUserWatchedMediaDto().copy(
            contentType = UserWatchedMediaDto.ContentType.TV_SHOW
        )

        val result = userWatchedMediaDto.toEntity(
            isSaved = false,
            listId = 1
        )

        assertThat(result.contentType).isEqualTo(UserWatchedMedia.ContentType.TV_SHOW)
    }

    @Test
    fun `should map to entity with empty genreIds when dto has no genres`() {
        val userWatchedMediaDto = createMockUserWatchedMediaDto().copy(genreIds = emptyList())

        val result = userWatchedMediaDto.toEntity(
            isSaved = false,
            listId = 1
        )

        assertThat(result.genreIds).isEmpty()
    }

    @Test
    fun `should map to entity with one genreId when dto has a single genre`() {
        val userWatchedMediaDto = createMockUserWatchedMediaDto().copy(genreIds = listOf(28L))

        val result = userWatchedMediaDto.toEntity(
            isSaved = false,
            listId = 1,
        )

        assertThat(result.genreIds.size).isEqualTo(1)
        assertThat(result.genreIds[0]).isEqualTo(28L)
    }

    @Test
    fun `should map to entity with all genreIds when dto has multiple genres`() {
        val userWatchedMediaDto = createMockUserWatchedMediaDto().copy(
            genreIds = listOf(28L, 12L, 16L, 35L, 80L)
        )

        val result = userWatchedMediaDto.toEntity(
            isSaved = false,
            listId = 1,
        )

        assertThat(result.genreIds.size).isEqualTo(5)
        assertThat(result.genreIds[0]).isEqualTo(28L)
        assertThat(result.genreIds[1]).isEqualTo(12L)
        assertThat(result.genreIds[2]).isEqualTo(16L)
        assertThat(result.genreIds[3]).isEqualTo(35L)
        assertThat(result.genreIds[4]).isEqualTo(80L)
    }

    @Test
    fun `should map imageUrl correctly when dto has different imageUrls`() {
        val userWatchedMediaDto1 = createMockUserWatchedMediaDto(contentImageUrl = "/movie1.jpg")
        val userWatchedMediaDto2 = createMockUserWatchedMediaDto(contentImageUrl = "/tv_show2.png")
        val userWatchedMediaDto3 = createMockUserWatchedMediaDto(contentImageUrl = "")

        val result1 = userWatchedMediaDto1.toEntity(
            isSaved = false,
            listId = 1,
        )
        val result2 = userWatchedMediaDto2.toEntity(
            isSaved = false,
            listId = 1
        )
        val result3 = userWatchedMediaDto3.toEntity(
            isSaved = false,
            listId = 1
        )

        assertThat(result1.contentImageUrl).isEqualTo("/movie1.jpg")
        assertThat(result2.contentImageUrl).isEqualTo("/tv_show2.png")
        assertThat(result3.contentImageUrl).isEmpty()
    }

    @Test
    fun `jUserWatchedMedia toDto should map correctly with valid data`() {
        val userWatchedMedia = createMockUserWatchedMedia()

        val result = userWatchedMedia.toDto()

        assertThat(result.contentId).isEqualTo(123L)
        assertThat(result.genreIds.size).isEqualTo(2)
        assertThat(result.genreIds[0]).isEqualTo(28L)
        assertThat(result.genreIds[1]).isEqualTo(12L)
        assertThat(result.contentImageUrl).isEqualTo("/content_image.jpg")
        assertThat(result.contentType).isEqualTo(UserWatchedMediaDto.ContentType.MOVIE)
        assertThat(result.userId).isEqualTo(123)
    }


    companion object {
        private fun createMockUserWatchedMediaDto(
            contentId: Long = 123L,
            genreIds: List<Long> = listOf(28L, 12L),
            contentImageUrl: String = "/content_image.jpg",
            contentType: UserWatchedMediaDto.ContentType = UserWatchedMediaDto.ContentType.MOVIE,
            userId: Long = 123L
        ) = UserWatchedMediaDto(
            contentId = contentId,
            genreIds = genreIds,
            contentImageUrl = contentImageUrl,
            contentType = contentType,
            userId = userId
        )

        private fun createMockUserWatchedMedia(
            contentId: Long = 123L,
            genreIds: List<Long> = listOf(28L, 12L),
            contentImageUrl: String = "/content_image.jpg",
            contentType: UserWatchedMedia.ContentType = UserWatchedMedia.ContentType.MOVIE,
            userId: Long = 123L
        ) = UserWatchedMedia(
            contentId = contentId,
            genreIds = genreIds,
            contentImageUrl = contentImageUrl,
            contentType = contentType,
            userId = userId,
            isSaved = false,
            listId = null
        )
    }
} 