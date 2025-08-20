package com.baghdad.repository.model

data class ReviewDto(
    val id: String,
    val authorDisplayName: String,
    val authorAvatarUrl: String,
    val authorUsername: String,
    val rating: Double,
    val reviewText: String,
    val postedDate: String
)