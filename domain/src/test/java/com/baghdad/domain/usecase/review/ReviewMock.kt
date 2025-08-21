package com.baghdad.domain.usecase.review

import com.baghdad.entity.media.Review

object ReviewMock {
    val REVIEW = Review(
        id = "1",
        authorName = "John Doe",
        authorAvatarUrl = "https://example.com/avatar.jpg",
        contentTitle = "Movie Title",
        reviewText = "Great movie!",
        postedDate = kotlinx.datetime.LocalDate(2023, 10, 1),
        rating = 9.5

    )
    val REVIEWS = listOf(
        REVIEW,
        REVIEW.copy(id = "2", authorName = "Jane Smith"),
        REVIEW.copy(id = "3", contentTitle = "Another Movie")
    )
}