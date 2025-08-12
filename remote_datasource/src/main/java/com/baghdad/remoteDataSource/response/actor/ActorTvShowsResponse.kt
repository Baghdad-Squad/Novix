package com.baghdad.remoteDataSource.response.actor

import com.google.gson.annotations.SerializedName
data class ActorTvShowsResponse(
    @SerializedName("cast") val cast: List<ActorTvShowDto>? = null,
) {
    data class ActorTvShowDto(
        @SerializedName("id") val id: Long? = null,
        @SerializedName("genre_ids") val genreIds: List<Long>? = null,
        @SerializedName("name") val name: String? = null,
        @SerializedName("overview") val overview: String? = null,
        @SerializedName("poster_path") val posterPath: String? = null,
        @SerializedName("vote_average") val voteAverage: Double? = null,
        @SerializedName("first_air_date") val firstAirDate: String? = null,
        @SerializedName("original_name") val originalName: String? = null,
    )
}

