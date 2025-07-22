package com.baghdad.viewmodel.people

import androidx.paging.PagingData
import com.baghdad.viewmodel.base.BaseUiState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

data class PeopleUiState(
    val people: Flow<PagingData<People>> = emptyFlow(),
    val currentPage: Int = 1,
    val endReached: Boolean = false,
    override val isLoading: Boolean = false,
    val isPaging: Boolean = false
) : BaseUiState {
    data class People(
        val id: Long,
        val profilePictureURL: String,
        val name: String
    )
}

