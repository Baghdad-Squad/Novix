package com.baghdad.viewmodel.profile

import com.baghdad.viewmodel.base.BaseViewModel
import com.baghdad.viewmodel.errorStates.BaseSnackBarMessage
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(

) : BaseViewModel<ProfileScreenUIState, ProfileEffect>(ProfileScreenUIState()),
    ProfileInteractionListener {
    override fun mapThrowableToErrorMessage(throwable: Throwable): BaseSnackBarMessage {
        TODO("Not yet implemented")
    }

    override fun onSnackBarActionLabelClick() {
        TODO("Not yet implemented")
    }

    override fun onclickWatchingHistory() {
        TODO("Not yet implemented")
    }

    override fun onclickMyRating() {
        TODO("Not yet implemented")
    }

    override fun onclickContentRestriction() {
        TODO("Not yet implemented")
    }

    override fun onclickChangePassword() {
        TODO("Not yet implemented")
    }

    override fun onclickAppearance() {
        TODO("Not yet implemented")
    }

    override fun onclickLanguage() {
        TODO("Not yet implemented")
    }

    override fun ontClickLogOut() {
        TODO("Not yet implemented")
    }
}