package com.baghdad.domain.model.savedList

import com.baghdad.entity.media.Movie

data class SavedMovie(
    val movie: Movie,
    val isSaved: Boolean,
    val listId: Long?
)
