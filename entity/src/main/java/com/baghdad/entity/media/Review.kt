package com.baghdad.entity.media

import kotlinx.datetime.LocalDate

data class Review(
    val id: String,
    val authorName: String,
    val authorAvatarUrl: String,
    val contentTitle: String,
    val rating: Float,
    val reviewText: String,
    val postedDate: LocalDate
)
