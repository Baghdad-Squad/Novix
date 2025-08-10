package com.baghdad.domain.model.savedList

import com.baghdad.entity.media.Movie

data class SavableMovie(
    val movie: Movie,
    val isSaved: Boolean,
    val listId: Long?,
)
