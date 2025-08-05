package com.baghdad.remoteDataSource.response.savedList

import com.google.gson.annotations.SerializedName

data class ListDetailsResponse(
    @SerializedName("created_by") val createdBy: String? = null,
    @SerializedName("description") val description: String? = null,
    @SerializedName("favorite_count") val favoriteCount: Int? = null,
    @SerializedName("id") val id: Long? = null,
    @SerializedName("items") val items: List<Item?>? = null,
    @SerializedName("item_count") val itemCount: Int? = null,
    @SerializedName("iso_639_1") val iso6391: String? = null,
    @SerializedName("name") val name: String? = null,
    @SerializedName("poster_path") val posterPath: String? = null,
    @SerializedName("page") val page: Int? = null,
    @SerializedName("total_pages") val totalPages: Int? = null,
    @SerializedName("total_results") val totalResults: Int? = null,
) {
    data class Item(
        @SerializedName("adult") val adult: Boolean? = null,
        @SerializedName("backdrop_path") val backdropPath: String? = null,
        @SerializedName("genre_ids") val genreIds: List<Long?>? = null,
        @SerializedName("id") val id: Long? = null,
        @SerializedName("media_type") val mediaType: String? = null,
        @SerializedName("original_language") val originalLanguage: String? = null,
        @SerializedName("original_title") val originalTitle: String? = null,
        @SerializedName("overview") val overview: String? = null,
        @SerializedName("popularity") val popularity: Double? = null,
        @SerializedName("poster_path") val posterPath: String? = null,
        @SerializedName("release_date") val releaseDate: String? = null,
        @SerializedName("title") val title: String? = null,
        @SerializedName("video") val video: Boolean? = null,
        @SerializedName("vote_average") val voteAverage: Double? = null,
        @SerializedName("vote_count") val voteCount: Int? = null,
    )
}
