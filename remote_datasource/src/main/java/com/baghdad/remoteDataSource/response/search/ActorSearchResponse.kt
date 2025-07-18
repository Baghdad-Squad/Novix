package com.baghdad.remoteDataSource.response.search

import kotlinx.serialization.Serializable

@Serializable
data class ActorSearchResponse(
    @SerialName("page") val page: Int? = null,
    @SerialName("results") val results: List<Result?>? = null,
    @SerialName("total_pages") val totalPages: Int? = null,
    @SerialName("total_results") val totalResults: Int? = null
) {
    @Serializable
    data class Result(
        @SerialName("adult") val adult: Boolean? = null,
        @SerialName("gender") val gender: Int? = null,
        @SerialName("id") val id: Int? = null,
        @SerialName("known_for_department") val knownForDepartment: String? = null,
        @SerialName("name") val name: String? = null,
        @SerialName("original_name") val originalName: String? = null,
        @SerialName("popularity") val popularity: Double? = null,
        @SerialName("profile_path") val profilePath: String? = null
    )
}