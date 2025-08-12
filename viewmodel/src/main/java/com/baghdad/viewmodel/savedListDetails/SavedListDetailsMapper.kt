package com.baghdad.viewmodel.savedListDetails

import com.baghdad.domain.model.savedList.SavedMovie
import com.baghdad.entity.savedList.SavedList

fun SavedMovie.toUIState() =
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
