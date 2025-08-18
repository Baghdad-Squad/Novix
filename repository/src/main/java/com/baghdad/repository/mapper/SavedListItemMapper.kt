package com.baghdad.repository.mapper

import com.baghdad.domain.model.savedList.SavedMovie
import com.baghdad.repository.model.savedList.SavableMovieDto

fun SavableMovieDto.toEntity(): SavedMovie {
    return SavedMovie(
        movie = movie.toEntity(),
        isSaved = isSaved,
        listId = listId
    )
}