package com.baghdad.repository.model.savedList

import com.baghdad.repository.model.MovieDto

data class SavableMovieDto(
    val movie: MovieDto,
    val isSaved: Boolean,
    val listId: Long?
)
