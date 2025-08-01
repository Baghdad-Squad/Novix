package com.baghdad.viewmodel.review

interface ReviewInteractionListener {
    fun onNavigateBack()
    fun onExpandedTextChange(id: String)
    fun onSnackBarActionLabelClick()
}