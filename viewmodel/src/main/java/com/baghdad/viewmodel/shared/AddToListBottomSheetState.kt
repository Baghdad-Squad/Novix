package com.baghdad.viewmodel.shared

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

data class AddToListBottomSheetState(
    val isVisible: Boolean = false,
    val isLoading: Boolean = false,
    val savedLists: Flow<PagingData<SavedListUiState>> = flowOf(),
    val selectedListId: Long? = null,
    val selectedItemId: Long = 0L,
)
