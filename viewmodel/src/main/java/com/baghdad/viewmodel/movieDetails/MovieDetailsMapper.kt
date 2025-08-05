package com.baghdad.viewmodel.movieDetails

import com.baghdad.domain.model.MediaAccountStates

fun MediaAccountStates.toUiState() =
    MovieDetailsState(
        isRated = isMediaRated,
    )
