package com.baghdad.repository.mapper

import com.baghdad.domain.model.continueWatching.UserWatchedMedia
import com.baghdad.repository.model.UserWatchedMediaDto
import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.Test

class ContinueWatchingMapperTest {

    @Test
    fun `should map to entity correctly when dto has valid data`() {
        // Given
        val continueWatchingDto = createMockContinueWatchingDto()

        // When
        val result = continueWatchingDto.toEntity()

        // Then
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
        // Given
        val userWatchedMediaDto = createMockContinueWatchingDto().copy(
            contentType = UserWatchedMediaDto.ContentType.TV_SHOW
        )

        // When
        val result = userWatchedMediaDto.toEntity()

        // Then
        assertThat(result.contentType).isEqualTo(UserWatchedMedia.ContentType.TV_SHOW)
    }

    @Test
    fun `should map to entity with empty genreIds when dto has no genres`() {
        // Given
        val continueWatchingDto = createMockContinueWatchingDto().copy(genreIds = emptyList())

        // When
        val result = continueWatchingDto.toEntity()

        // Then
        assertThat(result.genreIds).isEmpty()
    }

    @Test
    fun `should map to entity with one genreId when dto has a single genre`() {
        // Given
        val continueWatchingDto = createMockContinueWatchingDto().copy(genreIds = listOf(28L))

        // When
        val result = continueWatchingDto.toEntity()

        // Then
        assertThat(result.genreIds.size).isEqualTo(1)
        assertThat(result.genreIds[0]).isEqualTo(28L)
    }

    @Test
    fun `should map to entity with all genreIds when dto has multiple genres`() {
        // Given
        val continueWatchingDto = createMockContinueWatchingDto().copy(
            genreIds = listOf(28L, 12L, 16L, 35L, 80L)
        )

        // When
        val result = continueWatchingDto.toEntity()

        // Then
        assertThat(result.genreIds.size).isEqualTo(5)
        assertThat(result.genreIds[0]).isEqualTo(28L)
        assertThat(result.genreIds[1]).isEqualTo(12L)
        assertThat(result.genreIds[2]).isEqualTo(16L)
        assertThat(result.genreIds[3]).isEqualTo(35L)
        assertThat(result.genreIds[4]).isEqualTo(80L)
    }

    @Test
    fun `should map imageUrl correctly when dto has different imageUrls`() {
        // Given
        val continueWatchingDto1 = createMockContinueWatchingDto(contentImageUrl = "/movie1.jpg")
        val continueWatchingDto2 = createMockContinueWatchingDto(contentImageUrl = "/tv_show2.png")
        val continueWatchingDto3 = createMockContinueWatchingDto(contentImageUrl = "")

        // When
        val result1 = continueWatchingDto1.toEntity()
        val result2 = continueWatchingDto2.toEntity()
        val result3 = continueWatchingDto3.toEntity()

        // Then
        assertThat(result1.contentImageUrl).isEqualTo("/movie1.jpg")
        assertThat(result2.contentImageUrl).isEqualTo("/tv_show2.png")
        assertThat(result3.contentImageUrl).isEmpty()
    }

    @Test
    fun `Empty list of ContinueWatchingDto toEntities should return empty list`() {
        // Given
        val userWatchedMediaDtos = emptyList<UserWatchedMediaDto>()

        // When
        val result = userWatchedMediaDtos.toEntities()

        // Then
        assertThat(result).isEmpty()
    }

    @Test
    fun `ContinueWatching toDto should map correctly with valid data`() {
        // Given
        val continueWatching = createMockContinueWatching()

        // When
        val result = continueWatching.toDto()

        // Then
        assertThat(result.contentId).isEqualTo(123L)
        assertThat(result.genreIds.size).isEqualTo(2)
        assertThat(result.genreIds[0]).isEqualTo(28L)
        assertThat(result.genreIds[1]).isEqualTo(12L)
        assertThat(result.contentImageUrl).isEqualTo("/content_image.jpg")
        assertThat(result.contentType).isEqualTo(UserWatchedMediaDto.ContentType.MOVIE)
        assertThat(result.userId).isEqualTo(123)
    }



    companion object {
        private fun createMockContinueWatchingDto(
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

        private fun createMockContinueWatching(
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
            userId = userId
        )
    }
} 