package com.baghdad.viewmodel.search

import androidx.paging.PagingData
import com.baghdad.domain.model.search.RecentlyViewed.ContentType
import com.baghdad.viewmodel.base.BaseUiState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

data class SearchScreenState(
    val searchText: String = "",
    val isUserTyping: Boolean = false,
    var lastProcessedQuery: String = "",
    val selectedSearchTab: SearchTab = SearchTab.MOVIES,
    val moviesFlow: Flow<PagingData<MovieUiState>> = flowOf(),
    val tvShowsFlow: Flow<PagingData<TvShowUiState>> = flowOf(),
    val actorsFlow: Flow<PagingData<ActorUiState>> = flowOf(),
    val recentViewed: List<RecentlyViewedUiState> = emptyList(),
    val recentSearch: List<RecentSearchUiState> = emptyList(),
    val isLoading: Boolean = false,
) : BaseUiState {

    data class MovieUiState(
        val id: Long = 0,
        val posterPictureURL: String = "",
        val isSaved: Boolean = false
    )

    data class TvShowUiState(
        val id: Long = 0,
        val posterPictureURL: String = "",
        val isSaved: Boolean = false
    )

    data class ActorUiState(
        val id: Long = 0,
        val name: String = "",
        val profilePictureURL: String = ""
    )

    data class GenreUiState(
        val id: Long = 0,
        val name: String = ""
    )

    data class RecentlyViewedUiState(
        val id: Long = 0,
        val posterPictureURL: String = "",
        val contentType: ContentType = ContentType.MOVIE,
        val isSaved: Boolean = false
    )

    data class RecentSearchUiState(
        val id: Long = 0,
        val query: String = ""
    )

    enum class SearchTab {
        MOVIES,
        TV_SHOWS,
        ACTORS,
    }
}


