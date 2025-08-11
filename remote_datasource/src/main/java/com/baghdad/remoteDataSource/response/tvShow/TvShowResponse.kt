package com.baghdad.remoteDataSource.response.tvShow

import com.google.gson.annotations.SerializedName
data class TvShowResponse(
    @SerializedName("page") val page: Int? = null,
    @SerializedName("results") val results: List<TVShowDetailsResponse>? = null,
    @SerializedName("total_pages") val totalPages: Int? = null,
    @SerializedName("total_results") val totalResults: Int? = null,
)
