package com.baghdad.local_datasource.roomDB.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.baghdad.local_datasource.roomDB.converter.Converters
import com.baghdad.repository.model.GenreDto
import com.baghdad.repository.model.GenreDto.GenreType
import com.baghdad.repository.model.MovieDto

@Entity(tableName = "TrendingMovie")
data class TrendingMovie(
    @PrimaryKey(autoGenerate = false) val movieId: Long,
    val posterPictureURL: String,
    @TypeConverters(Converters::class) val genres: List<Long>,
)

fun TrendingMovie.toDto(): MovieDto {
    return MovieDto(
        id = this.movieId,
        genres = genres.map { GenreDto(name = "", id = it, type = GenreType.MOVIE) },
        imdbRating = 0.0,
        posterPictureURL = this.posterPictureURL,
        title = "",
        userRating = null,
        releaseDate = "",
        overview = "",
        trailerURL = "",
        runtimeMinutes = 0
    )
}


fun MovieDto.toTrendingMovie(): TrendingMovie {
    return TrendingMovie(
        movieId = id,
        posterPictureURL = posterPictureURL,
        genres = genres.map { it.id }
    )
}


