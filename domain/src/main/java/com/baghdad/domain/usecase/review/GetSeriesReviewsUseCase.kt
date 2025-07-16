package com.baghdad.domain.usecase.review

import com.baghdad.entity.media.Review
import kotlinx.datetime.LocalDate

class GetSeriesReviewsUseCase {

    suspend operator fun invoke(seriesId: Long): List<Review> = listOf(
        Review(
            id = "1",
            authorName = "CinephileHub",
            authorAvatarUrl = "https://via.placeholder.com/150",
            contentTitle = "Matrix Reloaded",
            reviewText = "Amazing movie about choice and control.",
            postedDate = LocalDate(1, 1, 1),
            rating = 9.2f
        ),
        Review(
            id = "2",
            authorName = "FilmGeek",
            authorAvatarUrl = "https://via.placeholder.com/150",
            contentTitle = "Matrix Revolutions",
            reviewText = "A bold finale with huge action set‑pieces.",
            postedDate = LocalDate(1, 1, 1),
            rating = 8.4f
        )
    )
}