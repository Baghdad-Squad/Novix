package com.baghdad.remoteDataSource.response.episode

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class EpisodeDetailsResponse(
    @SerialName("air_date")
    val airDate: String? = null,
    @SerialName("episode_number")
    val episodeNumber: Int = 0,
    @SerialName("name")
    val name: String? = null,
    @SerialName("overview")
    val overview: String? = null,
    @SerialName("id")
    val id: Int = 0,
    @SerialName("runtime")
    val runtime: Int = 0,
    @SerialName("season_number")
    val seasonNumber: Int = 0,
    @SerialName("vote_average")
    val voteAverage: Double = 0.0,
)

