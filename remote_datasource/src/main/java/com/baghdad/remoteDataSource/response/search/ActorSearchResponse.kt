package com.baghdad.remoteDataSource.response.search

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ActorSearchResponse(
    val page: Int? = null,
    val results: List<Result?>? = null,
    @SerialName("total_pages")
    val totalPages: Int? = null,
    @SerialName("total_results")
    val totalResults: Int? = null
) {
    @Serializable
    data class Result(
        val adult: Boolean? = null,
        val gender: Int? = null,
        val id: Int? = null,
        @SerialName("known_for_department")
        val knownForDepartment: String? = null,
        val name: String? = null,
        @SerialName("original_name")
        val originalName: String? = null,
        val popularity: Double? = null,
        @SerialName("profile_path")
        val profilePath: String? = null
    )
}