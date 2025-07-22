package com.baghdad.remoteDataSource.response.movie

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TrendingMovieResponse(
    @SerialName("page")
    val page: Int? = null,

    @SerialName("results")
    val results: List<Result>? = null,

    @SerialName("total_pages")
    val totalPages: Int? = null,

    @SerialName("total_results")
    val totalResults: Int? = null
) {
    @Serializable
    data class Result(
        @SerialName("id")
        val id: Long? = null,

        @SerialName("title")
        val title: String? = null,

        @SerialName("poster_path")
        val posterPath: String? = null,

        @SerialName("backdrop_path")
        val backdropPath: String? = null,

        @SerialName("overview")
        val overview: String? = null,

        @SerialName("release_date")
        val releaseDate: String? = null,

        @SerialName("vote_average")
        val voteAverage: Double? = null,

        @SerialName("vote_count")
        val voteCount: Int? = null,

        @SerialName("popularity")
        val popularity: Double? = null,

        @SerialName("genre_ids")
        val genreIds: List<Int>? = null
    )
}
