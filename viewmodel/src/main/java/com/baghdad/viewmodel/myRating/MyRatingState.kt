package com.baghdad.viewmodel.myRating

import androidx.paging.PagingData
import com.baghdad.viewmodel.base.BaseUiState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

data class MyRatingState(
    val isLoading: Boolean = true,
    val selectedMediaTab: MediaTab? = null,
    val mediaFlow: Flow<PagingData<MediaItemUiState>> = flowOf(),
    ) : BaseUiState {
    data class MediaItemUiState(
        val id: Long = 0,
        val posterPictureURL: String = "",
        val contentType: ContentType = ContentType.MOVIE,
        val rating: String = "0",
    )
    enum class MediaTab {
        MOVIE, TV_SHOW
    }
    enum class ContentType {
        MOVIE, TV_SHOW
    }
}