package com.baghdad.viewmodel.myLists

import androidx.paging.PagingData
import com.baghdad.viewmodel.base.BaseUiState
import com.baghdad.viewmodel.shared.AddListBottomSheetState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

data class MyListsScreenState(
    val isLoading: Boolean = false,
    val savedLists: Flow<PagingData<SavedListUiState>> = flowOf(),
    val isUsedLoggedIn: Boolean = true,
    val addListBottomSheetState: AddListBottomSheetState = AddListBottomSheetState(),
) : BaseUiState {
    data class SavedListUiState(
        val id: Long = 0L,
        val name: String = "",
        val itemCount: Int = 0,
    )
}
