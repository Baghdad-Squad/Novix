package com.baghdad.remoteDataSource.response

import com.google.gson.annotations.SerializedName

data class ReviewsResponse(
    @SerializedName("id")
    val id: Int? = 0,
    @SerializedName("page")
    val page: Int? = 0,
    @SerializedName("results")
    val results: List<ReviewResponse>? = null,
    @SerializedName("total_pages")
    val totalPages: Int? = 0,
    @SerializedName("total_results")
    val totalResults: Int? = 0
)

data class ReviewResponse(
    @SerializedName("author")
    val author: String? = null,
    @SerializedName("author_details")
    val authorDetails: MovieAuthorDetails? = null,
    @SerializedName("content")
    val content: String? = null,
    @SerializedName("created_at")
    val createdAt: String? = null,
    @SerializedName("id")
    val id: String? = null,
)

data class MovieAuthorDetails(
    @SerializedName("name")
    val name: String? = null,
    @SerializedName("username")
    val username: String? = null,
    @SerializedName("avatar_path")
    val avatarPath: String? = null,
    @SerializedName("rating")
    val rating: Float? = null
)
