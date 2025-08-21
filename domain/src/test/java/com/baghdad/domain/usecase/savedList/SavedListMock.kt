package com.baghdad.domain.usecase.savedList

import com.baghdad.domain.model.pagination.PagedResult
import com.baghdad.domain.model.savedList.SavedListDetails
import com.baghdad.domain.usecase.movie.MovieMock
import com.baghdad.entity.savedList.SavedList

object SavedListMock {

    val SAVED_LIST_ID = 1L
    val SAVED_LIST = SavedList(SAVED_LIST_ID, "My List", 2)

    val SAVED_LISTS = listOf(SAVED_LIST, SAVED_LIST.copy(id = 2L, name = "Watch Later"))


    val SAVED_LIST_DETAILS = SavedListDetails(
        savedList = SAVED_LIST,
        pagedItems = MovieMock.SAVED_MOVIES_PAGED_RESULT
    )

    val SAVED_LIST_RESULT = PagedResult(
        data = SAVED_LISTS,
        nextPage = 2,
        prevPage = null
    )

}