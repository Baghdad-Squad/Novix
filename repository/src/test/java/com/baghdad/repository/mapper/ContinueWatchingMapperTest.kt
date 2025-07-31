package com.baghdad.repository.mapper

import com.baghdad.domain.model.ContinueWatching
import com.baghdad.repository.model.ContinueWatchingDto
import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.Test

class ContinueWatchingMapperTest {

    @Test
    fun `ContinueWatchingDto toEntity should map correctly with valid data`() {
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
        assertThat(result.contentType).isEqualTo(ContinueWatching.ContentType.MOVIE)
        assertThat(result.userId).isEqualTo(123)
    }

    @Test
    fun `ContinueWatchingDto toEntity should handle TV_SHOW content type`() {
        // Given
        val continueWatchingDto = createMockContinueWatchingDto().copy(
            contentType = ContinueWatchingDto.ContentType.TV_SHOW
        )

        // When
        val result = continueWatchingDto.toEntity()

        // Then
        assertThat(result.contentType).isEqualTo(ContinueWatching.ContentType.TV_SHOW)
    }

    @Test
    fun `ContinueWatchingDto toEntity should handle empty genre IDs`() {
        // Given
        val continueWatchingDto = createMockContinueWatchingDto().copy(genreIds = emptyList())

        // When
        val result = continueWatchingDto.toEntity()

        // Then
        assertThat(result.genreIds).isEmpty()
    }

    @Test
    fun `ContinueWatchingDto toEntity should handle single genre ID`() {
        // Given
        val continueWatchingDto = createMockContinueWatchingDto().copy(genreIds = listOf(28L))

        // When
        val result = continueWatchingDto.toEntity()

        // Then
        assertThat(result.genreIds.size).isEqualTo(1)
        assertThat(result.genreIds[0]).isEqualTo(28L)
    }

    @Test
    fun `ContinueWatchingDto toEntity should handle multiple genre IDs`() {
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
    fun `ContinueWatchingDto toEntity should handle different content IDs`() {
        // Given
        val continueWatchingDto1 = createMockContinueWatchingDto(contentId = 1L)
        val continueWatchingDto2 = createMockContinueWatchingDto(contentId = 999999L)
        val continueWatchingDto3 = createMockContinueWatchingDto(contentId = 0L)

        // When
        val result1 = continueWatchingDto1.toEntity()
        val result2 = continueWatchingDto2.toEntity()
        val result3 = continueWatchingDto3.toEntity()

        // Then
        assertThat(result1.contentId).isEqualTo(1L)
        assertThat(result2.contentId).isEqualTo(999999L)
        assertThat(result3.contentId).isEqualTo(0L)
    }

    @Test
    fun `ContinueWatchingDto toEntity should handle different image URLs`() {
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
    fun `List of ContinueWatchingDto toEntities should map correctly`() {
        // Given
        val continueWatchingDtos = listOf(
            createMockContinueWatchingDto(contentId = 1L),
            createMockContinueWatchingDto(contentId = 2L),
            createMockContinueWatchingDto(contentId = 3L)
        )

        // When
        val result = continueWatchingDtos.toEntities()

        // Then
        assertThat(result.size).isEqualTo(3)
        assertThat(result[0].contentId).isEqualTo(1L)
        assertThat(result[1].contentId).isEqualTo(2L)
        assertThat(result[2].contentId).isEqualTo(3L)
    }

    @Test
    fun `Empty list of ContinueWatchingDto toEntities should return empty list`() {
        // Given
        val continueWatchingDtos = emptyList<ContinueWatchingDto>()

        // When
        val result = continueWatchingDtos.toEntities()

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
        assertThat(result.contentType).isEqualTo(ContinueWatchingDto.ContentType.MOVIE)
        assertThat(result.userId).isEqualTo(123)
    }

    @Test
    fun `ContinueWatching toDto should handle TV_SHOW content type`() {
        // Given
        val continueWatching = createMockContinueWatching().copy(
            contentType = ContinueWatching.ContentType.TV_SHOW
        )

        // When
        val result = continueWatching.toDto()

        // Then
        assertThat(result.contentType).isEqualTo(ContinueWatchingDto.ContentType.TV_SHOW)
    }

    @Test
    fun `ContinueWatching toDto should handle empty genre IDs`() {
        // Given
        val continueWatching = createMockContinueWatching().copy(genreIds = emptyList())

        // When
        val result = continueWatching.toDto()

        // Then
        assertThat(result.genreIds).isEmpty()
    }

    @Test
    fun `ContinueWatching toDto should handle different content IDs`() {
        // Given
        val continueWatching1 = createMockContinueWatching(contentId = 1L)
        val continueWatching2 = createMockContinueWatching(contentId = 999999L)
        val continueWatching3 = createMockContinueWatching(contentId = 0L)

        // When
        val result1 = continueWatching1.toDto()
        val result2 = continueWatching2.toDto()
        val result3 = continueWatching3.toDto()

        // Then
        assertThat(result1.contentId).isEqualTo(1L)
        assertThat(result2.contentId).isEqualTo(999999L)
        assertThat(result3.contentId).isEqualTo(0L)
    }

    @Test
    fun `ContinueWatching toDto should handle different image URLs`() {
        // Given
        val continueWatching1 = createMockContinueWatching(contentImageUrl = "/movie1.jpg")
        val continueWatching2 = createMockContinueWatching(contentImageUrl = "/tv_show2.png")
        val continueWatching3 = createMockContinueWatching(contentImageUrl = "")

        // When
        val result1 = continueWatching1.toDto()
        val result2 = continueWatching2.toDto()
        val result3 = continueWatching3.toDto()

        // Then
        assertThat(result1.contentImageUrl).isEqualTo("/movie1.jpg")
        assertThat(result2.contentImageUrl).isEqualTo("/tv_show2.png")
        assertThat(result3.contentImageUrl).isEmpty()
    }

    companion object {
        private fun createMockContinueWatchingDto(
            contentId: Long = 123L,
            genreIds: List<Long> = listOf(28L, 12L),
            contentImageUrl: String = "/content_image.jpg",
            contentType: ContinueWatchingDto.ContentType = ContinueWatchingDto.ContentType.MOVIE,
            userId: Long = 123L
        ) = ContinueWatchingDto(
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
            contentType: ContinueWatching.ContentType = ContinueWatching.ContentType.MOVIE,
            userId: Long = 123L
        ) = ContinueWatching(
            contentId = contentId,
            genreIds = genreIds,
            contentImageUrl = contentImageUrl,
            contentType = contentType,
            userId = userId
        )
    }
} 