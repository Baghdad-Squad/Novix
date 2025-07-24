package com.baghdad.remoteDataSource.response

import com.google.gson.annotations.SerializedName

data class SimilarMovieResponse(
    @SerializedName("page") val page: Int? = null,
    @SerializedName("results") val results: List<MovieResult>? = null,
    @SerializedName("total_pages") val totalPages: Int? = null,
    @SerializedName("total_results") val totalResults: Int? = null
)

data class MovieResult(
    @SerializedName("id") val id: Int? = null,
    @SerializedName("title") val title: String? = null,
    @SerializedName("genre_ids") val genreIds: List<Int>? = null,
    @SerializedName("vote_average") val voteAverage: Double? = null,
    @SerializedName("release_date") val releaseDate: String? = null,
    @SerializedName("overview") val overview: String? = null,
    @SerializedName("poster_path") val posterPath: String? = null,
)

