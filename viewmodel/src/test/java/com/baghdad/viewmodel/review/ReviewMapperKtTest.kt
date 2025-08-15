package com.baghdad.viewmodel.review

import com.baghdad.entity.media.Review
import com.google.common.truth.Truth.assertThat
import kotlinx.datetime.LocalDate
import org.junit.jupiter.api.Test

class ReviewUiMapperTest {

    @Test
    fun `toReviewUi() should correctly transform all fields when given valid Review domain model`() {
        // Given
        val review = Review(
            id = "r1",
            authorName = "Max Verstappen",
            authorAvatarUrl = "https://example.com/avatar.jpg",
            contentTitle = "Inception",
            reviewText = "Amazing plot!",
            postedDate = LocalDate(2023, 7, 15),
            rating = 8.78
        )

        // When
        val uiReview = review.toUiState()

        // Then
        assertThat(uiReview.id).isEqualTo("r1")
        assertThat(uiReview.authorName).isEqualTo("Max Verstappen")
        assertThat(uiReview.authorAvatarUrl).isEqualTo("https://example.com/avatar.jpg")
        assertThat(uiReview.contentTitle).isEqualTo("Inception")
        assertThat(uiReview.reviewText).isEqualTo("Amazing plot!")
        assertThat(uiReview.postedDate).isEqualTo("15-07-2023")
        assertThat(uiReview.rating).isWithin(0.01).of(8.8)
    }
}