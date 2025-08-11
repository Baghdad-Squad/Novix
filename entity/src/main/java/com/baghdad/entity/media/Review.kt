package com.baghdad.entity.media

import kotlinx.datetime.LocalDate

data class Review(
    val id: Long,
    val authorName: String,
    val authorAvatarUrl: String,
    val contentTitle: String,
    val rating: Double,
    val reviewText: String,
    val postedDate: LocalDate
)
