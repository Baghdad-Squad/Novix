package com.baghdad.local_datasource.roomDB.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.baghdad.local_datasource.roomDB.converter.Converters

@Entity(tableName = "Movie")
data class Movie(
    @PrimaryKey val id: Long,
    val title: String,
    @TypeConverters(Converters::class) val genres: List<String>,
    val imdbRating: Double,
    val userRating: Double?,
    val releaseDate: String,
    val overview: String,
    @TypeConverters(Converters::class) val cast: List<String>,
    val posterPictureURL: String,
    @TypeConverters(Converters::class) val backdropPicturesURLs: List<String>,
    val runtimeMinutes: Int
)