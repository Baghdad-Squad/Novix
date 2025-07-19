package com.baghdad.local_datasource.roomDB.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.baghdad.local_datasource.roomDB.converter.Converters
import com.baghdad.repository.model.GenreDto
import com.baghdad.repository.model.MovieDto

@Entity(tableName = "Movie")
data class Movie(
    @PrimaryKey val id: Long,
    val title: String,
    @TypeConverters(Converters::class) val genres: List<Long>,
    val imdbRating: Double,
    val userRating: Double?,
    val releaseDate: String,
    val overview: String,
    val posterPictureURL: String,
    val runtimeMinutes: Int
)

fun MovieDto.toLocalDto(): Movie = Movie(
    id = this.id,
    title = this.title,
    genres = this.genres.map { it.id },
    imdbRating = this.imdbRating,
    userRating = this.userRating,
    releaseDate = this.releaseDate,
    overview = this.overview,
    posterPictureURL = this.posterPictureURL,
    runtimeMinutes = this.runtimeMinutes
)

fun Movie.toDto(genres: List<GenreDto>): MovieDto = MovieDto(
    id = this.id,
    title = this.title,
    genres = genres,
    imdbRating = this.imdbRating,
    userRating = this.userRating,
    releaseDate = this.releaseDate,
    overview = this.overview,
    posterPictureURL = this.posterPictureURL,
    runtimeMinutes = this.runtimeMinutes,
    trailerURL = ""
)

fun List<Movie>.toDtos(genresMap: Map<Long, Genre>): List<MovieDto> {
    return this.map { movie ->
        val genres = movie.genres.mapNotNull { genreId ->
            genresMap[genreId]?.toDto()
        }
        movie.toDto(genres)
    }
}
