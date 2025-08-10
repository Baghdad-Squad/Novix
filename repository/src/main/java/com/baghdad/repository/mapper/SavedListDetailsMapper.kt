package com.baghdad.repository.mapper

import com.baghdad.domain.model.savedList.SavedListDetails
import com.baghdad.repository.model.savedList.SavableMovieDto
import com.baghdad.repository.model.savedList.SavedListDetailsDto

fun SavedListDetailsDto.toEntity() =
    SavedListDetails(
        savedList = savedList.toEntity(),
        pagedItems =
            pagedItems.toPagedResult(SavableMovieDto::toEntity),
    )
