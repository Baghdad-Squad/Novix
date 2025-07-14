package com.baghdad.local_datasource.database.dto

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.baghdad.local_datasource.Converters
import com.baghdad.repository.model.MovieDto

@Entity(tableName = "Movie")
data class LocalMovieDto(
    @PrimaryKey
    @ColumnInfo(name = ID)
    val id: Long,
    @ColumnInfo(name = TITLE)
    val title: String,
    @TypeConverters(Converters::class)
    @ColumnInfo(name = GENRES)
    val genres: List<String>,
    @ColumnInfo(name = IMDB_RATING)
    val imdbRating: Double,
    @ColumnInfo(name = USER_RATING)
    val userRating: Double?,
    @ColumnInfo(name = RELEASE_DATE)
    val releaseDate: String,
    @ColumnInfo(name = OVERVIEW)
    val overview: String,
    @ColumnInfo(name = POSTER_PICTURE_URL)
    val posterPictureURL: String,
    @ColumnInfo(name = RUNTIME_MINUTES)
    val runtimeMinutes: Int
) {
    companion object {
        const val ID = "id"
        const val TITLE = "title"
        const val GENRES = "genres"
        const val IMDB_RATING = "imdbRating"
        const val USER_RATING = "userRating"
        const val RELEASE_DATE = "releaseDate"
        const val OVERVIEW = "overview"
        const val POSTER_PICTURE_URL = "posterPictureURL"
        const val RUNTIME_MINUTES = "runtimeMinutes"
    }
}

fun MovieDto.toEntity(): LocalMovieDto = LocalMovieDto(
    id = this.id,
    title = this.title,
    genres = emptyList(),
    imdbRating = this.imdbRating,
    userRating = this.userRating,
    releaseDate = this.releaseDate,
    overview = this.overview,
    posterPictureURL = this.posterPictureURL,
    runtimeMinutes = this.runtimeMinutes
)

fun LocalMovieDto.toDto(): MovieDto = MovieDto(
    id = this.id,
    title = this.title,
    genres = emptyList(),
    imdbRating = this.imdbRating,
    userRating = this.userRating,
    releaseDate = this.releaseDate,
    overview = this.overview,
    posterPictureURL = this.posterPictureURL,
    runtimeMinutes = this.runtimeMinutes
)

