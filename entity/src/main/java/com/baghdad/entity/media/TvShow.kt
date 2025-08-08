package com.baghdad.entity.media

import kotlinx.datetime.LocalDate

data class TvShow(
    val id: Long,
    val title: String,
    val genres: List<Genre>,
    val averageRating: Double,
    val userRating: Int?,
    val releaseDate: LocalDate,
    val overview: String,
    val posterImageURL: String,
    val trailerURL: String,
    val headerImagesURLs: List<String>,
    val numberOfSeasons: Int,
)