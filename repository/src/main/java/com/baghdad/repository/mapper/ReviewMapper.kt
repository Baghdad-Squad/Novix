package com.baghdad.repository.mapper

import com.baghdad.entity.media.Review
import com.baghdad.repository.model.ReviewDto
import kotlinx.datetime.LocalDate

fun ReviewDto.toEntity(): Review {
    return Review(
        id = id,
        authorName = authorName,
        authorAvatarUrl = authorAvatarUrl,
        contentTitle = contentTitle,
        rating = rating,
        reviewText = reviewText,
        postedDate = LocalDate.parse(this.postedDate.substring(0, 10))
    )
}
