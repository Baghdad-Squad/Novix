package com.baghdad.repository.model

data class ReviewDto(
    val id: Long,
    val authorName: String,
    val authorAvatarUrl: String,
    val contentTitle: String,
    val rating: Double,
    val reviewText: String,
    val postedDate: String
)