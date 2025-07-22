package com.baghdad.remoteDataSource.response.people

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PopularPeopleResponse(
    @SerialName("page") val page: Int,
    @SerialName("results") val results: List<PersonDetails>,
    @SerialName("total_pages") val totalPages: Int?,
    @SerialName("total_results") val totalResults: Int?
)

@Serializable
data class PersonDetails(
    @SerialName("adult") val adult: Boolean,
    @SerialName("id") val id: Int,
    @SerialName("name") val name: String,
    @SerialName("original_name") val originalName: String,
    @SerialName("media_type") val mediaType: String,
    @SerialName("popularity") val popularity: Double,
    @SerialName("gender") val gender: Int,
    @SerialName("known_for_department") val knownForDepartment: String? = null,
    @SerialName("profile_path") val profilePath: String? = null
)

