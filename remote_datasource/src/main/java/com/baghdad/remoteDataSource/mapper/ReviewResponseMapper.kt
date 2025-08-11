package com.baghdad.remoteDataSource.mapper

import com.baghdad.remoteDataSource.response.reviews.ReviewsResponse
import com.baghdad.repository.model.ReviewDto

fun ReviewsResponse.ReviewResponse.toDto(): ReviewDto {
    return ReviewDto(
        id = id ?: 0L,
        authorName = (authorDetails?.name.takeIf { !it.isNullOrBlank() }) ?: "Guest",
        authorAvatarUrl = ("https://image.tmdb.org/t/p/w500" + authorDetails?.avatarPath.orEmpty()),
        contentTitle = authorDetails?.username ?: "",
        rating = authorDetails?.rating ?: 0.0,
        reviewText = content ?: "",
        postedDate = createdAt.takeIf { !it.isNullOrBlank() } ?: "0001-01-01"
    )
}

fun ReviewsResponse.toReviewDto(): List<ReviewDto> {
    return results.orEmpty().mapNotNull { it.takeIf { it.id != null }?.toDto() }
}
