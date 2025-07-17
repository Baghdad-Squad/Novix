package com.baghdad.remoteDataSource.response.tvShow

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SeasonDetailResponse(
    @SerialName("_id")
    val id: String? = null,
    @SerialName("air_date")
    val airDate: String? = null,
    @SerialName("episodes")
    val episodes: List<EpisodeResponse>? = null,
    @SerialName("name")
    val name: String? = null,
    @SerialName("overview")
    val overview: String? = null,
    @SerialName("id")
    val seasonId: Int? = 0,
    @SerialName("poster_path")
    val posterPath: String? = null,
    @SerialName("season_number")
    val seasonNumber: Int? = 0,
    @SerialName("vote_average")
    val voteAverage: Double? = 0.0
)

@Serializable
data class EpisodeResponse(
    @SerialName("air_date")
    val airDate: String? = null,
    @SerialName("episode_number")
    val episodeNumber: Int? = 0,
    @SerialName("id")
    val id: Int? = 0,
    @SerialName("name")
    val name: String? = null,
    @SerialName("overview")
    val overview: String? = null,
    @SerialName("production_code")
    val productionCode: String? = null,
    @SerialName("runtime")
    val runtime: Int? = 0,
    @SerialName("season_number")
    val seasonNumber: Int? = 0,
    @SerialName("show_id")
    val showId: Int? = 0,
    @SerialName("still_path")
    val stillPath: String? = null,
    @SerialName("vote_average")
    val voteAverage: Double? = 0.0,
    @SerialName("vote_count")
    val voteCount: Int? = 0,
    @SerialName("guest_stars")
    val guestStars: List<GuestStar>? = null
)

@Serializable
data class GuestStar(
    @SerialName("character")
    val character: String? = null,
    @SerialName("credit_id")
    val creditId: String? = null,
    @SerialName("order")
    val order: Int? = 0,
    @SerialName("adult")
    val adult: Boolean? = true,
    @SerialName("gender")
    val gender: Int? = 0,
    @SerialName("id")
    val id: Int? = 0,
    @SerialName("known_for_department")
    val knownForDepartment: String? = null,
    @SerialName("name")
    val name: String? = null,
    @SerialName("original_name")
    val originalName: String? = null,
    @SerialName("popularity")
    val popularity: Double? = 0.0,
    @SerialName("profile_path")
    val profilePath: String? = null
)
