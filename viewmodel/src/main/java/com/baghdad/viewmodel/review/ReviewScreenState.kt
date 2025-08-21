package com.baghdad.viewmodel.review

import androidx.annotation.Keep
import com.baghdad.viewmodel.base.BaseUiState

data class ReviewScreenState(
    val reviews: List<ReviewUiState> = emptyList<ReviewUiState>(),
    val isLoading: Boolean = false,
) : BaseUiState {

    data class ReviewUiState(
        val id: String = "",
        val authorName: String = "",
        val authorAvatarUrl: String = "",
        val authorUsername: String = "",
        val reviewText: String = "",
        val postedDate: String = "",
        val rating: Double = 0.0,
        val isExpanded: Boolean = false
    )
}

@Keep
enum class ContentType {
    MOVIE,
    SERIES
}
