package com.baghdad.repository.model


data class MovieDto(
    val id: Long,
    val title: String,
    val genres: List<GenreDto>,
    val imdbRating: Double,
    val userRating: Double?,
    val releaseDate: String,
    val overview: String,
    val cast: List<CastMemberDto>,
    val posterPictureURL: String,
    val backdropPicturesURLs: List<String>,
    val runtimeMinutes: Int,
)