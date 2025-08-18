package com.baghdad.repository.model.savedList

import com.baghdad.repository.model.PagedResultDto
import com.baghdad.repository.model.SavedListDto

data class SavedListDetailsDto(
    val savedList: SavedListDto,
    val pagedItems: PagedResultDto<SavableMovieDto>
)
