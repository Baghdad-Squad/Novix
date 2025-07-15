package com.baghdad.remoteDataSource.response.actor

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ActorMoviesResponse(
    @SerialName("cast")
    val cast: List<ActorMovieDto>? = null,
)

@Serializable
data class ActorMovieDto(
    @SerialName("id")
    val id: Int? = null,
    @SerialName("title")
    val title: String? = null,
    @SerialName("genre_ids")
    val genreIds: List<Int>? = null,
    @SerialName("vote_average")
    val voteAverage: Double? = null,
    @SerialName("release_date")
    val releaseDate: String? = null,
    @SerialName("overview")
    val overview: String? = null,
    @SerialName("poster_path")
    val posterPath: String? = null,
)
