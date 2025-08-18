package com.baghdad.remoteDataSource.response.search

import com.google.gson.annotations.SerializedName
data class ActorSearchResponse(
    @SerializedName("page") val page: Int? = null,
    @SerializedName("results") val results: List<Result?>? = null,
    @SerializedName("total_pages") val totalPages: Int? = null,
    @SerializedName("total_results") val totalResults: Int? = null,
) {
    data class Result( @SerializedName("id") val id: Long? = null,
        @SerializedName("adult") val adult: Boolean? = null,
        @SerializedName("gender") val gender: Int? = null,
        @SerializedName("known_for_department") val knownForDepartment: String? = null,
        @SerializedName("name") val name: String? = null,
        @SerializedName("original_name") val originalName: String? = null,
        @SerializedName("popularity") val popularity: Double? = null,
        @SerializedName("profile_path") val profilePath: String? = null,
    )
}