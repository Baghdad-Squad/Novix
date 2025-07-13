package com.baghdad.entity.media

import com.baghdad.entity.person.CastMember
import kotlinx.datetime.LocalDate

data class Movie(
    val id: Long,
    val title: String,
    val genres: List<Genre>,
    val averageRating: Double,
    val userRating: Double?,
    val releaseDate: LocalDate,
    val overview: String,
    val cast: List<CastMember>,
    val posterImageURL: String,
    val backdropImagesURLs: List<String>,
    val runtimeMinutes: Int
)
