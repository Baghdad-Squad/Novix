package com.baghdad.viewmodel.myRating

import com.baghdad.domain.model.userRating.RatedMedia
import com.baghdad.entity.media.Movie
import com.baghdad.entity.media.TvShow

fun Movie.toMediaItemUiState(): MyRatingState.MediaItemUiState {
    return MyRatingState.MediaItemUiState(
        id = id,
        posterPictureURL = posterImageURL,
        contentType = MyRatingState.ContentType.MOVIE,
        rating = userRating?.toInt().toString(),
    )
}

fun TvShow.toMediaItemUiState(): MyRatingState.MediaItemUiState {
    return MyRatingState.MediaItemUiState(
        id = id,
        posterPictureURL = posterImageURL,
        contentType = MyRatingState.ContentType.TV_SHOW,
        rating = userRating?.toInt().toString(),
    )
}

fun RatedMedia.toMediaItemUiState(): MyRatingState.MediaItemUiState {
    return MyRatingState.MediaItemUiState(
        id = id,
        posterPictureURL = posterImageURL,
        contentType = MyRatingState.ContentType.MOVIE,
        rating = userRating?.toInt().toString(),
    )

}