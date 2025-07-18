package com.baghdad.local_datasource.roomDB.entity


import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.baghdad.local_datasource.roomDB.converter.Converters
import com.baghdad.repository.model.GenreDto
import com.baghdad.repository.model.TvShowDto


@Entity(tableName = "TvShow")
data class TvShow(
    @PrimaryKey val id: Long,
    val title: String,
    @TypeConverters(Converters::class) val genres: List<Long>,
    val imdbRating: Double,
    val userRating: Double?,
    val releaseDate: String,
    val overview: String,
    val posterPictureURL: String,
    val numberOfSeasons: Int,
    val headerPictures: List<String>
)

fun TvShow.toDto(genres: List<GenreDto>): TvShowDto {
    return TvShowDto(
        id = this.id,
        title = this.title,
        genres = genres,
        imdbRating = this.imdbRating,
        userRating = this.userRating,
        releaseDate = releaseDate,
        overview = this.overview,
        posterPictureURL = this.posterPictureURL,
        numberOfSeasons = this.numberOfSeasons,
        headerPictures = this.headerPictures
    )
}

fun TvShowDto.toLocalDto(): TvShow {
    return TvShow(
        id = this.id,
        title = this.title,
        genres = this.genres.map { it.id },
        imdbRating = this.imdbRating,
        userRating = this.userRating,
        releaseDate = this.releaseDate,
        overview = this.overview,
        posterPictureURL = this.posterPictureURL,
        numberOfSeasons = this.numberOfSeasons,
        headerPictures = this.headerPictures
    )
}

fun List<TvShow>.toDtos(genresMap: Map<Long, Genre>): List<TvShowDto> {
    return this.map { tvShow ->
        val genres = tvShow.genres.mapNotNull { genreId ->
            genresMap[genreId]?.toDto()
        }
        tvShow.toDto(genres)
    }
}