package com.baghdad.entity.media

import kotlinx.datetime.LocalDate

data class Movie(
    val id: Long,
    val title: String,
    val genres: List<Genre>,
    val averageRating: Double,
    val userRating: Double?,
    val releaseDate: LocalDate,
    val overview: String,
    val posterImageURL: String,
    val trailerURL: String,
    val runtimeMinutes: Int
)
