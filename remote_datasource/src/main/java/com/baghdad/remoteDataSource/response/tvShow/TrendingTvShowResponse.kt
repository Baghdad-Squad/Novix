package com.baghdad.remoteDataSource.response.tvShow


import com.google.gson.annotations.SerializedName

data class TrendingTvShowsResponse(
    @SerializedName("page")
    val page: Int? = null,

    @SerializedName("results")
    val results: List<TrendingTvShow?>? = null,

    @SerializedName("total_pages")
    val totalPages: Int? = null,

    @SerializedName("total_results")
    val totalResults: Int? = null
) {
    data class TrendingTvShow(
        @SerializedName("adult")
        val adult: Boolean? = null,

        @SerializedName("backdrop_path")
        val backdropPath: String? = null,

        @SerializedName("id")
        val id: Int? = null,

        @SerializedName("name")
        val name: String? = null,

        @SerializedName("original_name")
        val originalName: String? = null,

        @SerializedName("overview")
        val overview: String? = null,

        @SerializedName("poster_path")
        val posterPath: String? = null,

        @SerializedName("media_type")
        val mediaType: String? = null,

        @SerializedName("original_language")
        val originalLanguage: String? = null,

        @SerializedName("genre_ids")
        val genreIds: List<Int>? = null,

        @SerializedName("popularity")
        val popularity: Double? = null,

        @SerializedName("first_air_date")
        val firstAirDate: String? = null,

        @SerializedName("vote_average")
        val voteAverage: Double? = null,

        @SerializedName("vote_count")
        val voteCount: Int? = null,

        @SerializedName("origin_country")
        val originCountry: List<String>? = null
    )
}
