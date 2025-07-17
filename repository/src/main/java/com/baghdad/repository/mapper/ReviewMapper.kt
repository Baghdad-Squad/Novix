package com.baghdad.repository.mapper

import com.baghdad.entity.media.Review
import com.baghdad.repository.model.ReviewDto
import kotlinx.datetime.toLocalDate

fun ReviewDto.toEntity(): Review {
    return Review(
        id = this.id,
        authorName = this.authorName,
        authorAvatarUrl = this.authorAvatarUrl,
        contentTitle = this.contentTitle,
        rating = this.rating,
        reviewText = this.reviewText,
        postedDate = this.postedDate.toLocalDate()
    )
}
