package com.baghdad.remoteDataSource.response.actor

import com.google.gson.annotations.SerializedName
data class TrendingActorResponse(
    @SerializedName("page") val page: Int? = null,
    @SerializedName("results") val results: List<TrendingActorDetails>? = null,
    @SerializedName("total_pages") val totalPages: Int? = null,
    @SerializedName("total_results") val totalResults: Int? = null
) {
    data class TrendingActorDetails(
        @SerializedName("id") val id: Long? = null,
        @SerializedName("adult") val adult: Boolean? = null,
        @SerializedName("name") val name: String? = null,
        @SerializedName("original_name") val originalName: String? = null,
        @SerializedName("media_type") val mediaType: String? = null,
        @SerializedName("popularity") val popularity: Double? = null,
        @SerializedName("gender") val gender: Int? = null,
        @SerializedName("known_for_department") val knownForDepartment: String? = null,
        @SerializedName("profile_path") val profilePath: String? = null
    )
}