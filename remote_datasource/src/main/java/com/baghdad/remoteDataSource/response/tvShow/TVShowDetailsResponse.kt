package com.baghdad.remoteDataSource.response.tvShow

import com.baghdad.remoteDataSource.response.movie.Genre
import com.google.gson.annotations.SerializedName
data class TVShowDetailsResponse(
    @SerializedName("genres")
    val genres: List<Genre>? = null,
    @SerializedName("id")
    val id: Int? = null,
    @SerializedName("name")
    val name: String? = null,
    @SerializedName("number_of_seasons")
    val numberOfSeasons: Int? = null,
    @SerializedName("overview")
    val overview: String? = null,
    @SerializedName("poster_path")
    val posterPath: String? = null,
    @SerializedName("vote_average")
    val voteAverage: Double? = null,
    @SerializedName("first_air_date")
    val firstAirDate: String? = null,
)



