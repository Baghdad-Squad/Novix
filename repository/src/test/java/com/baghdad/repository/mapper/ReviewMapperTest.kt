package com.baghdad.repository.mapper

import com.baghdad.repository.model.ReviewDto
import com.google.common.truth.Truth.assertThat
import kotlinx.datetime.LocalDate
import org.junit.jupiter.api.Test

class ReviewMapperTest {

    @Test
    fun `ReviewDto toEntity should map correctly with valid data`() {
        // Given
        val reviewDto = createMockReviewDto()

        // When
        val result = reviewDto.toEntity()

        // Then
        assertThat(result.id).isEqualTo("review123")
        assertThat(result.authorName).isEqualTo("John Reviewer")
        assertThat(result.authorAvatarUrl).isEqualTo("https://example.com/avatar.jpg")
        assertThat(result.contentTitle).isEqualTo("Great movie! Highly recommended.")
        assertThat(result.rating).isEqualTo(9.0)
        assertThat(result.reviewText).isEqualTo("This movie was fantastic! The plot was engaging and the acting was top-notch.")
        assertThat(result.postedDate).isEqualTo(LocalDate.parse("2023-01-01"))
    }

    @Test
    fun `ReviewDto toEntity should handle date with time component`() {
        // Given
        val reviewDto = createMockReviewDto().copy(postedDate = "2023-01-01T15:30:45.123Z")

        // When
        val result = reviewDto.toEntity()

        // Then
        assertThat(result.postedDate).isEqualTo(LocalDate.parse("2023-01-01"))
    }

    @Test
    fun `ReviewDto toEntity should handle date with different format`() {
        // Given
        val reviewDto = createMockReviewDto().copy(postedDate = "2023-12-25T00:00:00")

        // When
        val result = reviewDto.toEntity()

        // Then
        assertThat(result.postedDate).isEqualTo(LocalDate.parse("2023-12-25"))
    }

    @Test
    fun `ReviewDto toEntity should handle different author names`() {
        // Given
        val reviewDto1 = createMockReviewDto().copy(authorName = "Alice Smith")
        val reviewDto2 = createMockReviewDto().copy(authorName = "Bob Johnson")
        val reviewDto3 = createMockReviewDto().copy(authorName = "Anonymous")

        // When
        val result1 = reviewDto1.toEntity()
        val result2 = reviewDto2.toEntity()
        val result3 = reviewDto3.toEntity()

        // Then
        assertThat(result1.authorName).isEqualTo("Alice Smith")
        assertThat(result2.authorName).isEqualTo("Bob Johnson")
        assertThat(result3.authorName).isEqualTo("Anonymous")
    }

    @Test
    fun `ReviewDto toEntity should handle different content titles`() {
        // Given
        val reviewDto1 = createMockReviewDto().copy(contentTitle = "Amazing film!")
        val reviewDto2 = createMockReviewDto().copy(contentTitle = "Disappointing")
        val reviewDto3 = createMockReviewDto().copy(contentTitle = "Average movie")

        // When
        val result1 = reviewDto1.toEntity()
        val result2 = reviewDto2.toEntity()
        val result3 = reviewDto3.toEntity()

        // Then
        assertThat(result1.contentTitle).isEqualTo("Amazing film!")
        assertThat(result2.contentTitle).isEqualTo("Disappointing")
        assertThat(result3.contentTitle).isEqualTo("Average movie")
    }

    @Test
    fun `ReviewDto toEntity should handle different review texts`() {
        // Given
        val reviewDto1 = createMockReviewDto().copy(reviewText = "Short review")
        val reviewDto2 = createMockReviewDto().copy(reviewText = "Very long review with lots of details about the plot, characters, and overall experience...")
        val reviewDto3 = createMockReviewDto().copy(reviewText = "")

        // When
        val result1 = reviewDto1.toEntity()
        val result2 = reviewDto2.toEntity()
        val result3 = reviewDto3.toEntity()

        // Then
        assertThat(result1.reviewText).isEqualTo("Short review")
        assertThat(result2.reviewText).isEqualTo("Very long review with lots of details about the plot, characters, and overall experience...")
        assertThat(result3.reviewText).isEmpty()
    }

    @Test
    fun `ReviewDto toEntity should handle different avatar URLs`() {
        // Given
        val reviewDto1 = createMockReviewDto().copy(authorAvatarUrl = "https://example.com/avatar1.jpg")
        val reviewDto2 = createMockReviewDto().copy(authorAvatarUrl = "https://images.tmdb.org/avatar2.png")
        val reviewDto3 = createMockReviewDto().copy(authorAvatarUrl = "")

        // When
        val result1 = reviewDto1.toEntity()
        val result2 = reviewDto2.toEntity()
        val result3 = reviewDto3.toEntity()

        // Then
        assertThat(result1.authorAvatarUrl).isEqualTo("https://example.com/avatar1.jpg")
        assertThat(result2.authorAvatarUrl).isEqualTo("https://images.tmdb.org/avatar2.png")
        assertThat(result3.authorAvatarUrl).isEmpty()
    }

    companion object {
        private fun createMockReviewDto() = ReviewDto(
            id = "review123",
            contentTitle = "Great movie! Highly recommended.",
            authorName = "John Reviewer",
            rating = 9.0,
            authorAvatarUrl = "https://example.com/avatar.jpg",
            reviewText = "This movie was fantastic! The plot was engaging and the acting was top-notch.",
            postedDate = "2023-01-01"
        )
    }
} 