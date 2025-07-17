package com.baghdad.remoteDataSource.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ReviewsResponse(
    @SerialName("id")
    val id: Int = 0,
    @SerialName("page")
    val page: Int = 0,
    @SerialName("results")
    val results: List<ReviewResponse>? = null,
    @SerialName("total_pages")
    val totalPages: Int = 0,
    @SerialName("total_results")
    val totalResults: Int = 0
)

@Serializable
data class ReviewResponse(
    @SerialName("author")
    val author: String? = null,
    @SerialName("author_details")
    val authorDetails: MovieAuthorDetails? = null,
    @SerialName("content")
    val content: String? = null,
    @SerialName("created_at")
    val createdAt: String? = null,
    @SerialName("id")
    val id: String? = null,
)

@Serializable
data class MovieAuthorDetails(
    @SerialName("name")
    val name: String? = null,
    @SerialName("username")
    val username: String? = null,
    @SerialName("avatar_path")
    val avatarPath: String? = null,
    @SerialName("rating")
    val rating: String? = null
)
