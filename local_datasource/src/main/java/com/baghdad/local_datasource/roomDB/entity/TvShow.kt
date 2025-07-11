package com.baghdad.local_datasource.roomDB.entity

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.baghdad.local_datasource.roomDB.converter.Converters
import com.baghdad.repository.model.GenreDto
import com.baghdad.repository.model.MediaDto
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
    @TypeConverters(Converters::class) val releaseDate: LocalDate,
    val overview: String,
    @TypeConverters(Converters::class) val cast: List<String>,
    val posterPictureURL: String,
    @TypeConverters(Converters::class) val backdropPicturesURLs: List<String>,
    val numberOfSeasons: Int
)

fun TvShow.toDto(): TvShowDto {
    return TvShowDto(
        id = this.id,
        title = this.title,
        genres = emptyList(),
        imdbRating = this.imdbRating,
        userRating = this.userRating,
        releaseDate = releaseDate.toString(),
        overview = this.overview,
        cast = emptyList(),
        posterPictureURL = this.posterPictureURL,
        backdropPicturesURLs = this.backdropPicturesURLs,
        numberOfSeasons = this.numberOfSeasons,
    )
}

fun TvShow.toMediaDto(): MediaDto = TvShowDto(
    id = id,
    title = title,
    genres = genres.map { GenreDto(it.hashCode().toLong(), it) },
    imdbRating = imdbRating,
    userRating = userRating,
    releaseDate = releaseDate.toString(),
    overview = overview,
    cast = emptyList(),
    posterPictureURL = posterPictureURL,
    backdropPicturesURLs = backdropPicturesURLs,
    numberOfSeasons = numberOfSeasons
)

@RequiresApi(Build.VERSION_CODES.O)
fun TvShowDto.toEntity(): TvShow {
    return TvShow(
        id = this.id,
        title = this.title,
        genres = emptyList(),
        imdbRating = this.imdbRating,
        userRating = this.userRating,
        releaseDate = System.currentTimeMillis().let {
            LocalDate.parse(this.releaseDate, DateTimeFormatter.ISO_DATE)
        },
        overview = this.overview,
        cast = emptyList(),
        posterPictureURL = this.posterPictureURL,
        backdropPicturesURLs = this.backdropPicturesURLs,
        numberOfSeasons = this.numberOfSeasons
    )
}