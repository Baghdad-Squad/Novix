package com.baghdad.viewmodel.review

import com.baghdad.viewmodel.base.BaseUiState

data class ReviewScreenState(
    val reviews: List<ReviewUiState> = emptyList<ReviewUiState>(),
    override val isLoading: Boolean = false,
) : BaseUiState {

    data class ReviewUiState(
        val id: Long = -1,
        val authorName: String = "",
        val authorAvatarUrl: String = "",
        val contentTitle: String = "",
        val reviewText: String = "",
        val postedDate: String = "",
        val rating: Float = 0.0f,
        val isExpanded: Boolean = false
    )

}
