package com.baghdad.remoteDataSource.response.actor

import com.google.gson.annotations.SerializedName

data class TrendingActorResponse(
    val page: Int? = null,
    val results: List<TrendingActorDetails>? = null,
    @SerializedName("total_pages") val totalPages: Int? = null,
    @SerializedName("total_results") val totalResults: Int? = null
)

data class TrendingActorDetails(
    @SerializedName("adult") val adult: Boolean? = null,
    @SerializedName("id") val id: Int? = null,
    @SerializedName("name") val name: String? = null,
    @SerializedName("original_name") val originalName: String? = null,
    @SerializedName("media_type") val mediaType: String? = null,
    @SerializedName("popularity") val popularity: Double? = null,
    @SerializedName("gender") val gender: Int? = null,
    @SerializedName("known_for_department") val knownForDepartment: String? = null,
    @SerializedName("profile_path") val profilePath: String? = null
)
