package com.baghdad.viewmodel.savedListDetails

import com.baghdad.viewmodel.base.BaseViewModel
import com.baghdad.viewmodel.errorStates.BaseSnackBarMessage

class SavedListDetailsViewModel(

): BaseViewModel <SavedListDetailsScreenState, SavedListDetailsEffect>(SavedListDetailsScreenState())
{
    override fun mapThrowableToErrorMessage(throwable: Throwable): BaseSnackBarMessage {
        TODO("Not yet implemented")
    }
}