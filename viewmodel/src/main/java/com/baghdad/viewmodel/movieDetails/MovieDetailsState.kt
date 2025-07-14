package com.baghdad.viewmodel.movieDetails

import com.baghdad.viewmodel.base.BaseUiState
import com.baghdad.viewmodel.base.SnackBarState
import com.baghdad.viewmodel.errorStates.BaseErrorState

data class MovieDetailsState(
    val movieImages: List<String> = emptyList(),
    val movieName: String = "",
    val categories: List<String> = emptyList(),
    val rating: String = "",
    val duration: String = "",
    val date: String = "",
    val overView: String = "",
    val castes: List<ActorCardInfo>,
    val moreLikeThisMovie: List<String> = emptyList(),
    override val isLoading: Boolean = false,
    override val snackBarState: SnackBarState = SnackBarState(),
    override val baseErrorState: BaseErrorState? = null
): BaseUiState

data class ActorCardInfo(
    val name: String = "",
    val imageUrl: String? = null,
    val characterName: String = "",
    val id: Int = 0
)