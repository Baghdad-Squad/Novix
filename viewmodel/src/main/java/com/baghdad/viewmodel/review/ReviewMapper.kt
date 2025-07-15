package com.baghdad.viewmodel.review

import com.baghdad.entity.media.Review

fun Review.toReviewUi() = ReviewScreenState.ReviewUiState(
    authorName = authorName,
    authorAvatarUrl = authorAvatarUrl,
    contentTitle = contentTitle,
    reviewText = reviewText,
    postedDate = postedDate,
    rating = rating
)