package com.baghdad.repository.mapper

import com.baghdad.entity.media.Review
import com.baghdad.repository.model.ReviewDto
import kotlinx.datetime.LocalDate

fun ReviewDto.toEntity(): Review {
    return Review(
        id = id,
        authorDisplayName = authorDisplayName,
        authorAvatarUrl = authorAvatarUrl,
        authorUsername = authorUsername,
        rating = rating,
        reviewText = reviewText,
        postedDate = LocalDate.parse(input = postedDate.substring(0, 10))
    )
}