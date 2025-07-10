package com.baghdad.local_datasource.roomDB.entity

import androidx.room.Entity


@Entity(tableName = "Movie")
data class Movie(
    val id: Long,
    val title: String,
    val genres: List<String>,// Change to GenreDto
    val imdbRating: Double,
    val userRating: Double?,
    val releaseDate: String,
    val overview: String,
    val cast: List<String>, // Change to CastMemberDto
    val posterPictureURL: String,
    val backdropPicturesURLs: List<String>,
    val runtimeMinutes: Int
)
