package com.baghdad.viewmodel.review

import com.baghdad.entity.media.Review
import kotlinx.datetime.LocalDate
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class ReviewUiMapperTest {

    @Test
    fun `GIVEN Review domain model WHEN mapped to UI THEN fields are correctly transformed`() {
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
        val uiReview = review.toReviewUi()

        // Then
        assertEquals("r1", uiReview.id)
        assertEquals("Max Verstappen", uiReview.authorName)
        assertEquals("https://example.com/avatar.jpg", uiReview.authorAvatarUrl)
        assertEquals("Inception", uiReview.contentTitle)
        assertEquals("Amazing plot!", uiReview.reviewText)
        assertEquals("15-07-2023", uiReview.postedDate)
        assertEquals(8.8, uiReview.rating)
    }
}
