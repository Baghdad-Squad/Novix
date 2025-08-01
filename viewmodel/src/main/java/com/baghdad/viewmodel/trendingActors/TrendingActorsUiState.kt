package com.baghdad.viewmodel.trendingActors

import androidx.paging.PagingData
import com.baghdad.viewmodel.base.BaseUiState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

data class TrendingActorsUiState(
    val trendingActor: Flow<PagingData<TrendingActor>> = emptyFlow(),
    val isLoading: Boolean = false
) : BaseUiState {
    data class TrendingActor(
        val id: Long,
        val profilePictureURL: String,
        val name: String
    )
}

