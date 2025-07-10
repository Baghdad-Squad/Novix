package com.baghdad.local_datasource.roomDB.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.baghdad.local_datasource.roomDB.converter.Converters
import com.baghdad.repository.model.MovieDto

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

fun MovieDto.toEntity(): Movie = Movie(
    id = this.id,
    title = this.title,
    genres = emptyList(),
    imdbRating = this.imdbRating,
    userRating = this.userRating,
    releaseDate = this.releaseDate,
    overview = this.overview,
    cast = emptyList(),
    posterPictureURL = this.posterPictureURL,
    backdropPicturesURLs = emptyList(),
    runtimeMinutes = this.runtimeMinutes
)

fun Movie.toDto(): MovieDto = MovieDto(
    id = this.id,
    title = this.title,
    genres = emptyList(),
    imdbRating = this.imdbRating,
    userRating = this.userRating,
    releaseDate = this.releaseDate,
    overview = this.overview,
    cast = emptyList(),
    posterPictureURL = this.posterPictureURL,
    backdropPicturesURLs = this.backdropPicturesURLs,
    runtimeMinutes = this.runtimeMinutes
)
