package com.baghdad.viewmodel.myLists

import androidx.paging.PagingData
import com.baghdad.entity.savedList.SavedList
import com.baghdad.viewmodel.base.BaseUiState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

data class MyListsScreenState(
    val isLoading: Boolean = false,
    val savedLists: Flow<PagingData<SavedList>> = flowOf(),
    val isUsedLoggedIn: Boolean = false,
    val addListBottomSheetState: AddListBottomSheetState = AddListBottomSheetState(),
) : BaseUiState {
    data class SavedListUiState(
        val id: Long = 0L,
        val name: String = "",
        val itemCount: Int = 0,
    )

    data class AddListBottomSheetState(
        val isVisible: Boolean = false,
        val isLoading: Boolean = false,
        val listName: String = "",
    )
}
