package com.baghdad.remoteDataSource.response.episode

import com.google.gson.annotations.SerializedName

data class EpisodeDetailsResponse(
    @SerializedName("air_date")
    val airDate: String? = null,
    @SerializedName("episode_number")
    val episodeNumber: Int = 0,
    @SerializedName("name")
    val name: String? = null,
    @SerializedName("overview")
    val overview: String? = null,
    @SerializedName("id")
    val id: Int = 0,
    @SerializedName("runtime")
    val runtime: Int = 0,
    @SerializedName("season_number")
    val seasonNumber: Int = 0,
    @SerializedName("vote_average")
    val voteAverage: Double = 0.0,
)

