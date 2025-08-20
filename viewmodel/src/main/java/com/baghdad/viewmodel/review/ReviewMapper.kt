package com.baghdad.viewmodel.review

import com.baghdad.entity.media.Review
import com.baghdad.viewmodel.util.roundToFirstDecimal
import com.baghdad.viewmodel.util.toDDMMYYYYFormat

fun Review.toUiState() = ReviewScreenState.ReviewUiState(
    id = id ,
    authorName = authorDisplayName,
    authorAvatarUrl = authorAvatarUrl,
    contentTitle = authorUsername,
    reviewText = reviewText,
    postedDate = postedDate.toDDMMYYYYFormat(),
    rating = rating.roundToFirstDecimal()
)