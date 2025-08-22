package com.baghdad.domain.usecase.review

import com.baghdad.entity.media.Review

object ReviewMock {
    val REVIEW = Review(
        id = "1",
        authorDisplayName = "John Doe",
        authorAvatarUrl = "https://example.com/avatar.jpg",
        authorUsername = "Movie Title",
        reviewText = "Great movie!",
        postedDate = kotlinx.datetime.LocalDate(2023, 10, 1),
        rating = 9.5

    )
    val REVIEWS = listOf(
        REVIEW,
        REVIEW.copy(id = "2", authorDisplayName = "Jane Smith"),
        REVIEW.copy(id = "3", authorDisplayName = "Bob Johnson")
    )
}