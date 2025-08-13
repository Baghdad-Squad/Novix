package com.baghdad.repository.model

data class EpisodeDto(
    val id: Long,
    val title: String,
    val episodeNumber: Int,
    val rating: Double,
    val duration: String,
    val releasedDate: String?,
    val trailerUrl: String,
    val currentSeason: Int,
    val overview: String,
    val genres: List<GenreDto>,
    val userRating: Int?,
    val headerPictures: List<String>
)