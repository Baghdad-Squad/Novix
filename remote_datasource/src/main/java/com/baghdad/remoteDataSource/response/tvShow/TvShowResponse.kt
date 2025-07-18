package com.baghdad.remoteDataSource.response.tvShow

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TvShowResponse(
    @SerialName("page")
    val page: Int? = null,
    @SerialName("results")
    val results: List<TVShowDetailsResponse>? = null,
    @SerialName("total_pages")
    val totalPages: Int? = null,
    @SerialName("total_results")
    val totalResults: Int? = null
)
