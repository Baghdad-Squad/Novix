package com.baghdad.viewmodel.profile

import com.baghdad.domain.exception.NoInternetException
import com.baghdad.domain.usecase.login.LogOutUseCase
import com.baghdad.viewmodel.R
import com.baghdad.viewmodel.base.BaseViewModel
import com.baghdad.viewmodel.errorStates.BaseSnackBarMessage
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val logOutUseCase: LogOutUseCase
) : BaseViewModel<ProfileScreenUIState, ProfileEffect>(ProfileScreenUIState()),
    ProfileInteractionListener {

    init {
        loadData()
    }

    private fun loadData() {

    }

    override fun mapThrowableToErrorMessage(throwable: Throwable): BaseSnackBarMessage =
        BaseSnackBarMessage.UnknownError


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
        tryToExecute(
            callee = { logOutUseCase.invoke() },
            onSuccess = ::onSuccess,
            onError = { onError(it) },
        )
    }

    private fun onSuccess(result: Boolean) {
        updateState { profileScreenUIState ->
            profileScreenUIState.copy(
                isLogin = result
            )
        }
    }

    private fun onError(throwable: Throwable) {
        when (throwable) {
            is NoInternetException -> showNoInternetSnackBar()
            else -> handleError(throwable)
        }
    }

    override fun onSnackBarActionLabelClick() {
        TODO("Not yet implemented")
    }

    private fun showNoInternetSnackBar() {
        showSnackBar(
            message = BaseSnackBarMessage.NetworkError,
            actionLabelRes = R.string.retry,
            isSuccess = false,
            durationMillis = Int.MAX_VALUE.toLong(),
        )
    }
}