package com.baghdad.local_datasource.roomDB.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.baghdad.local_datasource.roomDB.converter.Converters
import com.baghdad.repository.model.GenreDto
import com.baghdad.repository.model.TvShowDto


@Entity(tableName = "trending_tv_shows")
data class TrendingTvShow(
    @PrimaryKey val tvShowId: Long,
    @TypeConverters(Converters::class) val genres: List<Long>,
    val posterPictureURL: String,
)


fun TrendingTvShow.toDto(genres: List<GenreDto>): TvShowDto =
    TvShowDto(
        id = this.tvShowId,
        genres = genres,
        imdbRating = 0.0,
        posterPictureURL = this.posterPictureURL,
        title = "",
        userRating = null,
        releaseDate = "",
        overview = "",
        trailerURL = "",
        numberOfSeasons = 0,
        headerImagesURLs = emptyList()
    )

fun TvShowDto.toLocalDtos(): TrendingTvShow =
    TrendingTvShow(
        tvShowId = this.id,
        genres = this.genres.map { it.id },
        posterPictureURL = this.posterPictureURL,
    )