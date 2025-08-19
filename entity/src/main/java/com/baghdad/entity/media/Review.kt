package com.baghdad.entity.media

import kotlinx.datetime.LocalDate

data class Review(
    val id: String,
    val authorName: String,
    val authorAvatarUrl: String,
    val authorUsername: String,
    val rating: Double,
    val reviewText: String,
    val postedDate: LocalDate
)
