package com.baghdad.remoteDataSource.response

import Genre
import kotlinx.serialization.SerialName


data class TVShowResponse(
    @SerialName("adult")
    val adult: Boolean? = null,
    @SerialName("backdrop_path")
    val backdropPath: String? = null,
    @SerialName("episode_run_time")
    val episodeRunTime: List<Int>? = null,
    @SerialName("first_air_date")
    val firstAirDate: String? = null,
    @SerialName("genres")
    val genres: List<Genre>? = null,
    @SerialName("homepage")
    val homepage: String? = null,
    @SerialName("id")
    val id: Int? = null,
    @SerialName("in_production")
    val inProduction: Boolean? = null,
    @SerialName("languages")
    val languages: List<String>? = null,
    @SerialName("last_air_date")
    val lastAirDate: String? = null,
    @SerialName("last_episode_to_air")
    val lastEpisodeToAir: EpisodeResponse? = null,
    @SerialName("name")
    val name: String? = null,
    @SerialName("next_episode_to_air")
    val nextEpisodeToAir: EpisodeResponse? = null,
    @SerialName("number_of_episodes")
    val numberOfEpisodes: Int? = null,
    @SerialName("number_of_seasons")
    val numberOfSeasons: Int? = null,
    @SerialName("origin_country")
    val originCountry: List<String>? = null,
    @SerialName("original_language")
    val originalLanguage: String? = null,
    @SerialName("original_name")
    val originalName: String? = null,
    @SerialName("overview")
    val overview: String? = null,
    @SerialName("popularity")
    val popularity: Double? = null,
    @SerialName("poster_path")
    val posterPath: String? = null,
    @SerialName("status")
    val status: String? = null,
    @SerialName("tagline")
    val tagline: String? = null,
    @SerialName("type")
    val type: String? = null,
    @SerialName("vote_average")
    val voteAverage: Double? = null,
    @SerialName("vote_count")
    val voteCount: Int? = null
)

