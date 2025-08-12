package com.baghdad.remoteDataSource.response.movie

import com.google.gson.annotations.SerializedName
data class TrendingMovieResponse(
    @SerializedName("page") val page: Int? = null,
    @SerializedName("results") val results: List<Result>? = null,
    @SerializedName("total_pages") val totalPages: Int? = null,
    @SerializedName("total_results") val totalResults: Int? = null,
) {
    data class Result(
        @SerializedName("id") val id: Long? = null,
        @SerializedName("title") val title: String? = null,
        @SerializedName("poster_path") val posterPath: String? = null,
        @SerializedName("backdrop_path") val backdropPath: String? = null,
        @SerializedName("overview") val overview: String? = null,
        @SerializedName("release_date") val releaseDate: String? = null,
        @SerializedName("vote_average") val voteAverage: Double? = null,
        @SerializedName("vote_count") val voteCount: Int? = null,
        @SerializedName("popularity") val popularity: Double? = null,
        @SerializedName("genre_ids") val genreIds: List<Long>? = null,
    )
}
