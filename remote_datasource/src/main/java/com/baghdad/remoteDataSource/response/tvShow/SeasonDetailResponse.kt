package com.baghdad.remoteDataSource.response.tvShow

import com.google.gson.annotations.SerializedName

data class SeasonDetailResponse(
    @SerializedName("episodes")
    val episodes: List<EpisodeResponse>? = null,
)

data class EpisodeResponse(
    @SerializedName("air_date")
    val airDate: String? = null,
    @SerializedName("episode_number")
    val episodeNumber: Int? = 0,
    @SerializedName("id")
    val id: Int? = 0,
    @SerializedName("name")
    val name: String? = null,
    @SerializedName("overview")
    val overview: String? = null,
    @SerializedName("runtime")
    val runtime: Int? = 0,
    @SerializedName("season_number")
    val seasonNumber: Int? = 0,
    @SerializedName("vote_average")
    val voteAverage: Double? = 0.0,
)
