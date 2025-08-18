package com.baghdad.repository.model


data class TvShowDto(
    val id: Long,
    val title: String,
    val genres: List<GenreDto>,
    val imdbRating: Double,
    val userRating: Int?,
    val releaseDate: String,
    val overview: String,
    val posterPictureURL: String,
    val trailerURL: String,
    val headerImagesURLs: List<String>,
    val numberOfSeasons: Int
)