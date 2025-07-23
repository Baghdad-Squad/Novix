package com.baghdad.local_datasource.roomDB.entity

import androidx.room.Entity
import androidx.room.TypeConverters
import com.baghdad.local_datasource.roomDB.converter.Converters
import com.baghdad.repository.model.GenreDto
import com.baghdad.repository.model.MovieDto

@Entity(primaryKeys = ["movieId"])
data class TopRatedMovie(
    val movieId: Long,
    @TypeConverters(Converters::class) val genres: List<Long>,
    val imdbRating: Double,
    val posterPictureURL: String,
    )

fun TopRatedMovie.toDto(genres: List<GenreDto>): MovieDto =
    MovieDto(
        id = this.movieId,
        genres = genres,
        imdbRating = this.imdbRating,
        posterPictureURL = this.posterPictureURL,
        title = "",
        userRating = null,
        releaseDate = "",
        overview = "",
        trailerURL = "",
        runtimeMinutes = 0
        )

fun MovieDto.toLocalDtos(): TopRatedMovie =
    TopRatedMovie(
        movieId = this.id,
        genres = this.genres.map { it.id },
        imdbRating = this.imdbRating,
        posterPictureURL = this.posterPictureURL,
    )
