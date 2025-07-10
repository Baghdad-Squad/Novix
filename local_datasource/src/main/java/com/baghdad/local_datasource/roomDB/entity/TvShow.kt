package com.baghdad.local_datasource.roomDB.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.baghdad.local_datasource.roomDB.converter.Converters
import java.time.LocalDate

@Entity(tableName = "TvShow")
data class TvShow(
    @PrimaryKey val id: Long,
    val title: String,
    @TypeConverters(Converters::class) val genres: List<String>,
    val imdbRating: Double,
    val userRating: Double?,
    @TypeConverters(Converters::class) val releaseDate: LocalDate,
    val overview: String,
    @TypeConverters(Converters::class) val cast: List<String>,
    val posterPictureURL: String,
    @TypeConverters(Converters::class) val backdropPicturesURLs: List<String>,
    val numberOfSeasons: Int
)