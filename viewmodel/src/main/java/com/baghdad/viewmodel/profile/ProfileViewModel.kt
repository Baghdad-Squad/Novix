package com.baghdad.viewmodel.profile

import com.baghdad.domain.exception.NoInternetException
import com.baghdad.domain.usecase.login.IsLoggedInUseCase
import com.baghdad.domain.usecase.login.LogOutUseCase
import com.baghdad.domain.usecase.login.LoggedInUserUseCase
import com.baghdad.entity.User
import com.baghdad.viewmodel.R
import com.baghdad.viewmodel.base.BaseViewModel
import com.baghdad.viewmodel.errorStates.BaseSnackBarMessage
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val logOutUseCase: LogOutUseCase,
    private val loggedInUserUseCase: LoggedInUserUseCase,
    private val isLoggedInUserUseCase: IsLoggedInUseCase,
) : BaseViewModel<ProfileScreenUIState, ProfileEffect>(ProfileScreenUIState()),
    ProfileInteractionListener {

    init {
        loadData()
    }

    private fun loadData() {
        tryToExecute(
            callee = { isLoggedInUserUseCase.invoke() },
            onSuccess = ::onSuccessLogin,
            onError = ::onError,
        )
    }

    private fun onSuccessLogin(result: Boolean) {
        updateState { profileScreenUIState ->
            profileScreenUIState.copy(
                isLogin = result
            )
        }
        if (result) {
            getUserInfo()
        }
    }

    private fun getUserInfo() {
        tryToExecute(
            callee = { loggedInUserUseCase.invoke() },
            onSuccess = ::onSuccessLoadData,
            onError = ::onError,
        )
    }

    private fun onSuccessLoadData(user: User?) {
        user?.let {
            updateState { profileScreenUIState ->
                profileScreenUIState.copy(
                    userInfo = ProfileScreenUIState.User(
                        userName = user.userName,
                        imageUrl = user.imageUrl
                    )
                )
            }
        }
    }

    override fun mapThrowableToErrorMessage(throwable: Throwable): BaseSnackBarMessage =
        BaseSnackBarMessage.UnknownError


    override fun onclickWatchingHistory() {
        sendEffect(ProfileEffect.NavigateToWatchingHistory)
    }

    override fun onclickMyRating() {
        sendEffect(ProfileEffect.NavigateToMyRatings)
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

    override fun ontClickLogIn() {
        sendEffect(ProfileEffect.NavigateToLogin)
    }
    override fun ontClickLogOut() {
        tryToExecute(
            callee = { logOutUseCase.invoke() },
            onSuccess = ::onSuccessLogOut,
            onError = ::onError,
        )
    }

    private fun onSuccessLogOut(result: Boolean) {
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