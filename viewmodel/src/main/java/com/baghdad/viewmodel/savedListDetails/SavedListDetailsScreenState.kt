package com.baghdad.viewmodel.savedListDetails

import androidx.paging.PagingData
import com.baghdad.viewmodel.base.BaseUiState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

data class SavedListDetailsScreenState(
    val mediaFlow: Flow<PagingData<SavedListDetailsMovieUiState>> = flowOf(),
    val selectedTab: SavedListTab = SavedListTab.ALL,
    val isLoading: Boolean = false,
    val savedList: SavedListUiState = SavedListUiState(),
) : BaseUiState {

    data class SavedListUiState(
        val id: Long = 0,
        val name: String = "",
        val itemCount: Int = 0
    )

    data class SavedListDetailsMovieUiState(
        val id: Long = 0,
        val posterUrl: String = "",
        val name: String = "",
        val contentType: ContentType
    ) {
        enum class ContentType {
            MOVIE,
            TV_SHOW
        }
    }
}

enum class SavedListTab { ALL, MOVIES, TV_SHOWS }
