package com.baghdad.remoteDataSource.response.actor

import com.google.gson.annotations.SerializedName

data class ActorMoviesResponse(
    @SerializedName("cast")
    val cast: List<ActorMovieDto>? = null,
)

data class ActorMovieDto(
    @SerializedName("id")
    val id: Int? = null,
    @SerializedName("title")
    val title: String? = null,
    @SerializedName("genre_ids")
    val genreIds: List<Int>? = null,
    @SerializedName("vote_average")
    val voteAverage: Double? = null,
    @SerializedName("release_date")
    val releaseDate: String? = null,
    @SerializedName("overview")
    val overview: String? = null,
    @SerializedName("poster_path")
    val posterPath: String? = null,
)
