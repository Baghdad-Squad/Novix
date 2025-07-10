package com.baghdad.remote_datasource.entity

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SearchResponse(
    @SerialName("page")
    val pageNumber: Int? = null,

    @SerialName("results")
    val results: List<MultiSearchItem> = emptyList(),

    @SerialName("total_results")
    val totalResults: Int? = null,

    @SerialName("total_pages")
    val totalPages: Int? = null
)

@Serializable
data class MultiSearchItem(
    @SerialName("id")
    val id: Long? = null,

    @SerialName("media_type")
    val mediaType: String? = null,// TODO

    @SerialName("overview")
    val movieOverview: String? = null,

    @SerialName("title")
    val movieTitle: String? = null,

    @SerialName("name")
    val tvShowName: String? = null,

    @SerialName("release_date")
    val releaseDate: String? = null,

    @SerialName("first_air_date")
    val firstAirDate: String? = null,

    @SerialName("original_name")
    val tvShowOriginalName: String? = null,

    @SerialName("poster_path")
    val tvShowPosterPath: String? = null,

    @SerialName("profile_path")
    val profilePath: String? = null,

    @SerialName("known_for_department")
    val knownForDepartment: String? = null,

    @SerialName("gender")
    val gender: Int? = null
)
