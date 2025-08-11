package com.baghdad.repository.mapper

import com.baghdad.domain.model.savedList.SavableMovie
import com.baghdad.repository.model.savedList.SavableMovieDto

fun SavableMovieDto.toEntity(): SavableMovie {
    return SavableMovie(
        movie = movie.toEntity(),
        isSaved = isSaved,
        listId = listId
    )
}