package com.baghdad.entity.media

import com.baghdad.entity.person.CastMember
import kotlinx.datetime.LocalDate

data class Movie(
    override val id: Long,
    override val title: String,
    override val genres: List<Genre>,
    override val imdbRating: Double,
    override val userRating: Double?,
    override val releaseDate: LocalDate,
    override val overview: String,
    override val cast: List<CastMember>,
    override val posterPictureURL: String,
    override val backdropPicturesURLs: List<String>,
    val runtimeMinutes: Int
) : Media
