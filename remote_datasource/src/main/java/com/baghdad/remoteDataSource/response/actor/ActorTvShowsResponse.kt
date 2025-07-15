package com.baghdad.remoteDataSource.response.actor

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ActorTvShowsResponse(
    @SerialName("cast")
    val cast: List<ActorTvShowDto>? = null,
)

@Serializable
data class ActorTvShowDto(
    @SerialName("id")
    val id: Int? = null,
    @SerialName("name")
    val name: String? = null,
    @SerialName("genre_ids")
    val genreIds: List<Int>? = null,
    @SerialName("overview")
    val overview: String? = null,
    @SerialName("poster_path")
    val posterPath: String? = null,
    @SerialName("vote_average")
    val voteAverage: Double? = null,
    @SerialName("first_air_date")
    val firstAirDate: String? = null,
    @SerialName("original_name")
    val originalName: String? = null,
)


