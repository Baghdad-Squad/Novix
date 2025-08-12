package com.baghdad.viewmodel.myRating

import com.baghdad.domain.model.RatedMedia
import com.baghdad.viewmodel.myRating.MyRatingState.ContentType

fun RatedMedia.toMediaItemUiState(): MyRatingState.MediaItemUiState {
    return MyRatingState.MediaItemUiState(
        id = id,
        posterPictureURL = posterImageURL,
        contentType = ContentType.valueOf(contentType.name),
        rating = userRating?.toInt().toString(),
    )

}