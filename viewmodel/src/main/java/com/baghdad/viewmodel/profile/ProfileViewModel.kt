package com.baghdad.viewmodel.profile

import com.baghdad.viewmodel.base.BaseViewModel
import com.baghdad.viewmodel.errorStates.BaseSnackBarMessage

class ProfileViewModel(

) : BaseViewModel<ProfileScreenUIState, ProfileEffect>(ProfileScreenUIState()),
    ProfileInteractionListener {
    override fun mapThrowableToErrorMessage(throwable: Throwable): BaseSnackBarMessage {
        TODO("Not yet implemented")
    }

}