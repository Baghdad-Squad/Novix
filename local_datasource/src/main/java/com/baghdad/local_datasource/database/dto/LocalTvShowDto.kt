package com.baghdad.local_datasource.database.dto

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.baghdad.local_datasource.Converters
import com.baghdad.local_datasource.database.dto.LocalTvShowDto.Companion.TV_SHOW_TABLE_NAME
import com.baghdad.repository.model.TvShowDto

@Entity(tableName = TV_SHOW_TABLE_NAME)
data class LocalTvShowDto(
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
    @TypeConverters(Converters::class)
    @ColumnInfo(name = RELEASE_DATE)
    val releaseDate: String,
    @ColumnInfo(name = OVERVIEW)
    val overview: String,
    @ColumnInfo(name = POSTER_PICTURE_URL)
    val posterPictureURL: String,
    @ColumnInfo(name = NUMBER_OF_SEASONS)
    val numberOfSeasons: Int
) {
    companion object {
        const val TV_SHOW_TABLE_NAME = "TvShow"
        const val ID = "id"
        const val TITLE = "title"
        const val GENRES = "genres"
        const val IMDB_RATING = "imdbRating"
        const val USER_RATING = "userRating"
        const val RELEASE_DATE = "releaseDate"
        const val OVERVIEW = "overview"
        const val POSTER_PICTURE_URL = "posterPictureURL"
        const val NUMBER_OF_SEASONS = "numberOfSeasons"
    }
}

fun LocalTvShowDto.toDto(): TvShowDto {
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

fun TvShowDto.toEntity(): LocalTvShowDto {
    return LocalTvShowDto(
        id = this.id,
        title = this.title,
        genres = emptyList(),
        imdbRating = this.imdbRating,
        userRating = this.userRating,
        releaseDate = this.releaseDate,
        overview = this.overview,
        posterPictureURL = this.posterPictureURL,
        numberOfSeasons = this.numberOfSeasons
    )
}