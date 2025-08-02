package com.baghdad.remoteDataSource.response.movie

import com.google.gson.annotations.SerializedName

data class MovieDetailsResponse(
    @SerializedName("id") val id: Long? = null,
    @SerializedName("title") val title: String? = null,
    @SerializedName("genres") val genres: List<Genre>? = null,
    @SerializedName("vote_average") val voteAverage: Double? = null,
    @SerializedName("release_date") val releaseDate: String? = null,
    @SerializedName("overview") val overview: String? = null,
    @SerializedName("poster_path") val posterPath: String? = null,
    @SerializedName("runtime") val runtime: Int? = null
)

data class Genre(
    @SerializedName("id") val id: Long? = null,
    @SerializedName("name") val name: String? = null
)
