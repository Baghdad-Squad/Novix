package com.baghdad.remoteDataSource.response.actor

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TrendingActorResponse(
    val page: Int? = null,
    val results: List<TrendingActorDetails>? = null,
    @SerialName("total_pages") val totalPages: Int? = null,
    @SerialName("total_results") val totalResults: Int? = null
)

@Serializable
data class TrendingActorDetails(
    @SerialName("adult") val adult: Boolean? = null,
    @SerialName("id") val id: Int? = null,
    @SerialName("name") val name: String? = null,
    @SerialName("original_name") val originalName: String? = null,
    @SerialName("media_type") val mediaType: String? = null,
    @SerialName("popularity") val popularity: Double? = null,
    @SerialName("gender") val gender: Int? = null,
    @SerialName("known_for_department") val knownForDepartment: String? = null,
    @SerialName("profile_path") val profilePath: String? = null
)
