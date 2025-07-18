package com.baghdad.remoteDataSource.response.movie

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MovieDetailsResponse(
    @SerialName("id") val id: Long? = null,
    @SerialName("title") val title: String? = null,
    @SerialName("genres") val genres: List<Genre>? = null,
    @SerialName("vote_average") val voteAverage: Double? = null,
    @SerialName("release_date") val releaseDate: String? = null,
    @SerialName("overview") val overview: String? = null,
    @SerialName("poster_path") val posterPath: String? = null,
    @SerialName("runtime") val runtime: Int? = null
)

@Serializable
data class Genre(
    @SerialName("id") val id: Long? = null,
    @SerialName("name") val name: String? = null
)
