package com.baghdad.viewmodel.topTvShowPicks

import com.baghdad.entity.media.TvShow

fun TvShow.toUIState() = TopTvShowPicksState.TvShowUiState(
    id = id,
    posterPictureURL = posterImageURL,
)

fun List<TvShow>.toUIStateList() = map { it.toUIState() }