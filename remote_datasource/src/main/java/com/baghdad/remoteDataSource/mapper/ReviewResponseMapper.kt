package com.baghdad.remoteDataSource.mapper

import com.baghdad.remoteDataSource.response.ReviewResponse
import com.baghdad.repository.model.ReviewDto

fun ReviewResponse.toDto(): ReviewDto {
    return ReviewDto(
        id = id?.toLongOrNull() ?: 0L,
        authorName = authorDetails?.name ?: "",
        authorAvatarUrl =  authorDetails?.avatarPath ?:  "",
        contentTitle = authorDetails?.username ?: "",
        rating = authorDetails?.rating?.toFloatOrNull() ?: 0f,
        reviewText = content ?: "",
        postedDate = createdAt ?: ""
    )
}
