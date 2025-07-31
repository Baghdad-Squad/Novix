package com.baghdad.repository.mapper

import com.baghdad.repository.model.ReviewDto
import com.google.common.truth.Truth.assertThat
import kotlinx.datetime.LocalDate
import org.junit.jupiter.api.Test

class ReviewMapperTest {

    @Test
    fun `should map correctly to entity when ReviewDto contains valid data`() {
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