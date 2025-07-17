package com.baghdad.remoteDataSource.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class EpisodeDetailsResponse(
    @SerialName("air_date") val airDate: String? = null,
    @SerialName("episode_number") val episodeNumber: Int = 0,
    @SerialName("name") val name: String? = null,
    @SerialName("overview") val overview: String? = null,
    @SerialName("id") val id: Int = 0,
    @SerialName("production_code") val productionCode: String? = null,
    @SerialName("runtime") val runtime: Int = 0,
    @SerialName("season_number") val seasonNumber: Int = 0,
    @SerialName("still_path") val stillPath: String? = null,
    @SerialName("vote_average") val voteAverage: Double = 0.0,
    @SerialName("vote_count") val voteCount: Int = 0
)

