package com.baghdad.entity.media

import kotlinx.datetime.LocalDate

data class Episode(
    val id: Long,
    val title: String,
    val episodeNumber: Int,
    val rating: Int,
    val duration: String,
    val releasedDate: LocalDate?,
    val trailerUrl: String,
    val currentSeason: Int,
    val userRating: Int?,
    val overview: String,
    val genres: List<Genre>,
    val headerPictures: List<String>,
)