package com.baghdad.viewmodel.people

import androidx.paging.PagingData
import com.baghdad.viewmodel.base.BaseUiState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

data class TrendingActorUiState(
    val trendingActor: Flow<PagingData<TrendingActor>> = emptyFlow(),
    override val isLoading: Boolean = false
) : BaseUiState {
    data class TrendingActor(
        val id: Long,
        val profilePictureURL: String,
        val name: String
    )
}

