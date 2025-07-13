package com.baghdad.local_datasource.roomDB.entity

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.baghdad.local_datasource.roomDB.converter.Converters
import com.baghdad.repository.model.TvShowDto
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Entity(tableName = "TvShow")
data class TvShow(
    @PrimaryKey val id: Long,
    val title: String,
    @TypeConverters(Converters::class) val genres: List<String>,
    val imdbRating: Double,
    val userRating: Double?,
    @TypeConverters(Converters::class) val releaseDate: String,
    val overview: String,
    val posterPictureURL: String,
    val numberOfSeasons: Int
)

fun TvShow.toDto(): TvShowDto {
    return TvShowDto(
        id = this.id,
        title = this.title,
        genres = emptyList(),
        imdbRating = this.imdbRating,
        userRating = this.userRating,
        releaseDate = releaseDate,
        overview = this.overview,
        posterPictureURL = this.posterPictureURL,
        numberOfSeasons = this.numberOfSeasons,
    )
}

@RequiresApi(Build.VERSION_CODES.O)
fun TvShowDto.toEntity(): TvShow {
    return TvShow(
        id = this.id,
        title = this.title,
        genres = emptyList(),
        imdbRating = this.imdbRating,
        userRating = this.userRating,
        releaseDate = LocalDate.parse(this.releaseDate, DateTimeFormatter.ISO_DATE).toString(),
        overview = this.overview,
        posterPictureURL = this.posterPictureURL,
        numberOfSeasons = this.numberOfSeasons
    )
}