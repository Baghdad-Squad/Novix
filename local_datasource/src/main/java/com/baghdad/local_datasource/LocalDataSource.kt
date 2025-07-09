package com.baghdad.local_datasource

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate

@Entity(tableName = "Tv Show")
data class TvShowEntity(
    @PrimaryKey val id: Long,
    val title: String,
    val genres: List<String> = emptyList(),
    val imdbRating: Double,
    val userRating: Double?,
    val releaseDate: LocalDate,
    val overview: String,
    val cast: List<String> = emptyList(),
    val posterPictureURL: String,
    val backdropPicturesURLs: List<String>,
    val numberOfSeasons: Int
)
