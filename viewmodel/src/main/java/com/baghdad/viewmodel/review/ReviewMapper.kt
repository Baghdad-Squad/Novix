package com.baghdad.viewmodel.review

import com.baghdad.entity.media.Review
import com.baghdad.viewmodel.movieDetails.roundToFirstDecimal
import com.baghdad.viewmodel.util.toDDMMYYYYFormat

fun Review.toReviewUi() = ReviewScreenState.ReviewUiState(
    id = id,
    authorName = authorName,
    authorAvatarUrl = authorAvatarUrl,
    contentTitle = contentTitle,
    reviewText = reviewText,
    postedDate = postedDate.toDDMMYYYYFormat(),
    rating = rating.toDouble().roundToFirstDecimal()
)