package com.baghdad.viewmodel.review

interface ReviewInteractionListener {
    fun onNavigateBack()
    fun onExpandedTextChange(id: Long)
    fun onSnackBarActionLabelClick()
}