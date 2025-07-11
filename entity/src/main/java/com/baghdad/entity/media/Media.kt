package com.baghdad.entity.media

import com.baghdad.entity.person.CastMember
import kotlinx.datetime.LocalDate

interface Media {
    val id: Long
    val title: String
    val genres: List<Genre>
    val imdbRating: Double
    val userRating: Double?
    val releaseDate: LocalDate
    val overview: String
    val cast: List<CastMember>
    val posterPictureURL: String
    val backdropPicturesURLs: List<String>
    val type: MediaType

    enum class MediaType {
        MOVIE,
        TV_SHOW
    }
}