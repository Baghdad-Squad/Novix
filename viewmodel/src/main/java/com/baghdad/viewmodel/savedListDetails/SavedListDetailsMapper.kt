package com.baghdad.viewmodel.savedListDetails

import com.baghdad.domain.model.savedList.SavableMovie
import com.baghdad.entity.savedList.SavedList

fun SavableMovie.toUIState() =
    SavedListDetailsScreenState.SavedListDetailsMovieUiState(
        id = this.movie.id,
        name = this.movie.title,
        posterUrl = this.movie.posterImageURL,
    )

fun SavedList.toUIState() =
    SavedListDetailsScreenState.SavedListUiState(
        id = this.id,
        name = this.name,
        itemCount = this.itemCount,
    )
