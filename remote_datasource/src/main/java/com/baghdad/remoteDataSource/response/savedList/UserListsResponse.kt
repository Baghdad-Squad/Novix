package com.baghdad.remoteDataSource.response.savedList

import com.google.gson.annotations.SerializedName
data class UserListsResponse(
    @SerializedName("page") val page: Int? = null,
    @SerializedName("results") val results: List<UserListDto>? = null,
    @SerializedName("total_pages") val totalPages: Int? = null,
    @SerializedName("total_results") val totalResults: Int? = null,
) {
    data class UserListDto(
        @SerializedName("id") val id: Long? = null,
        @SerializedName("description") val description: String? = null,
        @SerializedName("favorite_count") val favoriteCount: Int? = null,
        @SerializedName("item_count") val itemCount: Int? = null,
        @SerializedName("iso_639_1") val langCode: String? = null,
        @SerializedName("list_type") val listType: String? = null,
        @SerializedName("name") val name: String? = null,
        @SerializedName("poster_path") val posterPath: String? = null,
    )
}