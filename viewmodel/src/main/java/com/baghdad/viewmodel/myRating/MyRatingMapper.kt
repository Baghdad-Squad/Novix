package com.baghdad.viewmodel.myRating

import com.baghdad.domain.model.userRating.RatedMedia
import com.baghdad.viewmodel.myRating.MyRatingState.ContentType

fun RatedMedia.toUiState(): MyRatingState.MediaItemUiState {
    return MyRatingState.MediaItemUiState(
        id = id,
        posterPictureURL = posterImageURL,
        contentType = ContentType.valueOf(contentType.name),
        rating = userRating?.toInt().toString()
    )

}