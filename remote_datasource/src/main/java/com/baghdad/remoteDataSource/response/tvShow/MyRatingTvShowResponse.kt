package com.baghdad.remoteDataSource.response.tvShow

import com.google.gson.annotations.SerializedName

data class MyRatingTvShowResponse(
    @SerializedName("page") val page: Int? = null,
    @SerializedName("results") val results: List<TvShowItem>? = null,
    @SerializedName("total_pages") val totalPages: Int? = null,
    @SerializedName("total_results") val totalResults: Int? = null
){
    data class TvShowItem(
        @SerializedName("adult") val adult: Boolean? = null,
        @SerializedName("backdrop_path") val backdropPath: String? = null,
        @SerializedName("genre_ids") val genreIds: List<Long>? = null,
        @SerializedName("id") val id: Int? = null,
        @SerializedName("origin_country") val originCountry: List<String>? = null,
        @SerializedName("original_language") val originalLanguage: String? = null,
        @SerializedName("original_name") val originalName: String? = null,
        @SerializedName("overview") val overview: String? = null,
        @SerializedName("popularity") val popularity: Double? = null,
        @SerializedName("poster_path") val posterPath: String? = null,
        @SerializedName("first_air_date") val firstAirDate: String? = null,
        @SerializedName("name") val name: String? = null,
        @SerializedName("vote_average") val voteAverage: Double? = null,
        @SerializedName("vote_count") val voteCount: Int? = null,
        @SerializedName("rating") val rating: Int? = null
    )
}