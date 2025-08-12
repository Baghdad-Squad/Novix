package com.baghdad.remoteDataSource.mapper.review

import com.baghdad.remoteDataSource.response.reviews.ReviewsResponse
import com.baghdad.remoteDataSource.util.getImageUrlFromPath
import com.baghdad.repository.model.ReviewDto

fun ReviewsResponse.toReviewDto(): List<ReviewDto> {
    return results.orEmpty().mapNotNull { it.toReviewDtoIfValid() }
}

private fun ReviewsResponse.ReviewResponse?.toReviewDtoIfValid(): ReviewDto? {
    return this?.takeIf { id != null }?.toReviewDto()
}

private fun ReviewsResponse.ReviewResponse.toReviewDto(): ReviewDto {
    return ReviewDto(
        id = id.orEmpty(),
        authorName = authorDetails?.name.takeIf { !it.isNullOrBlank() }.orEmpty(),
        authorAvatarUrl = getImageUrlFromPath(authorDetails?.avatarPath),
        contentTitle = authorDetails?.username.orEmpty(),
        rating = authorDetails?.rating ?: 0.0,
        reviewText = content.orEmpty(),
        postedDate = createdAt.takeUnless { it.isNullOrBlank() } ?: "0001-01-01",
    )
}
