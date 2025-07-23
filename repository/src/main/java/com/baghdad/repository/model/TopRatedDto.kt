package com.baghdad.repository.model

data class TopRatedDto(
    val movieId: Long,
    val genres: List<Long>,
    val imdbRating: Double,
    val posterPictureURL: String,
)
