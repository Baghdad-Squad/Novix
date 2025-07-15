package com.baghdad.remoteDataSource.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SimilarMovieResponse(
    @SerialName("page")
    val page: Int? = null,
    @SerialName("results")
    val results: List<MovieResult>? = null,
    @SerialName("total_pages")
    val totalPages: Int? = null,
    @SerialName("total_results")
    val totalResults: Int? = null
)

@Serializable
data class MovieResult(
    @SerialName("id")
    val id: Int? = null,
    @SerialName("title")
    val title: String? = null,
    @SerialName("genre_ids")
    val genreIds: List<Int>? = null,
    @SerialName("vote_average")
    val voteAverage: Double? = null,
    @SerialName("release_date")
    val releaseDate: String? = null,
    @SerialName("overview")
    val overview: String? = null,
    @SerialName("poster_path")
    val posterPath: String? = null,
)

