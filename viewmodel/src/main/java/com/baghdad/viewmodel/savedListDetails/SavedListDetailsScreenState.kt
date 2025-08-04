package com.baghdad.viewmodel.savedListDetails

import androidx.paging.PagingData
import com.baghdad.viewmodel.base.BaseUiState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

data class SavedListDetailsScreenState(
    val mediaFlow: Flow<PagingData<SavedListDetailsMovieUiState>> = flowOf(),
    val selectedTab: SavedListTab = SavedListTab.ALL
) : BaseUiState {
    data class SavedListDetailsMovieUiState(
        val id: Long = 0,
        val name: String = "",
        val isSaved: Boolean = false,
        val contentType: ContentType
    ) {
        enum class ContentType {
            MOVIE,
            TV_SHOW
        }
    }
}

enum class SavedListTab { ALL, MOVIES, TV_SHOWS }
